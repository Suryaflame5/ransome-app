package app.scene.mobile

import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/** Minimal Supabase REST/Auth client. Uses only the public publishable/anon key. */
object SceneBackend {
    private val baseUrl: String get() = BuildConfig.SUPABASE_URL.trimEnd('/')
    private val publicKey: String get() = BuildConfig.SUPABASE_ANON_KEY
    val isConfigured: Boolean get() = baseUrl.startsWith("https://") && publicKey.isNotBlank()

    data class Mission(
        val id: String,
        val title: String,
        val summary: String,
        val category: String,
        val estimatedMinutes: Int,
        val pointsReward: Int,
        val city: String
    )

    data class AuthSession(val accessToken: String, val refreshToken: String, val userId: String)

    suspend fun fetchMissions(city: String? = null): Result<List<Mission>> = withContext(Dispatchers.IO) {
        runCatching {
            check(isConfigured) { "SCENE backend is not configured yet." }
            val cityFilter = if (city.isNullOrBlank()) "" else "&city=eq." + URLEncoder.encode(city, "UTF-8")
            val endpoint = "$baseUrl/rest/v1/missions?select=id,title,summary,category,estimated_minutes,points_reward,city&status=eq.published$cityFilter&order=created_at.desc"
            val json = request(endpoint, "GET")
            val rows = JSONArray(json)
            (0 until rows.length()).map { index ->
                val row = rows.getJSONObject(index)
                Mission(
                    id = row.getString("id"),
                    title = row.getString("title"),
                    summary = row.getString("summary"),
                    category = row.optString("category", "Discovery"),
                    estimatedMinutes = row.optInt("estimated_minutes", 15),
                    pointsReward = row.optInt("points_reward", 20),
                    city = row.optString("city", "")
                )
            }
        }
    }

    suspend fun signUp(email: String, password: String): Result<String> = withContext(Dispatchers.IO) {
        runCatching {
            check(isConfigured) { "SCENE backend is not configured yet." }
            require(email.contains('@')) { "Enter a valid email address." }
            require(password.length >= 8) { "Password must be at least 8 characters." }
            request("$baseUrl/auth/v1/signup", "POST", JSONObject().put("email", email).put("password", password).toString())
        }
    }

    suspend fun signIn(email: String, password: String): Result<AuthSession> = withContext(Dispatchers.IO) {
        runCatching {
            check(isConfigured) { "SCENE backend is not configured yet." }
            val query = URLEncoder.encode("password", "UTF-8")
            val body = JSONObject().put("email", email).put("password", password).toString()
            val json = JSONObject(request("$baseUrl/auth/v1/token?grant_type=$query", "POST", body))
            AuthSession(json.getString("access_token"), json.optString("refresh_token"), json.getJSONObject("user").getString("id"))
        }
    }

    private fun request(endpoint: String, method: String, body: String? = null): String {
        val connection = (URL(endpoint).openConnection() as HttpURLConnection).apply {
            requestMethod = method
            connectTimeout = 12000
            readTimeout = 12000
            setRequestProperty("apikey", publicKey)
            setRequestProperty("Authorization", "Bearer $publicKey")
            setRequestProperty("Accept", "application/json")
            if (body != null) {
                doOutput = true
                setRequestProperty("Content-Type", "application/json")
            }
        }
        try {
            if (body != null) connection.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }
            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val response = stream?.bufferedReader()?.use { it.readText() }.orEmpty()
            if (code !in 200..299) error("Supabase request failed ($code): $response")
            return response
        } finally {
            connection.disconnect()
        }
    }
}
