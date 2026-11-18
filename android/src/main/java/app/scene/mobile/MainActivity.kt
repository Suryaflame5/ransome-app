package app.scene.mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Paper = Color(0xFFF5F2EA)
private val Ink = Color(0xFF171714)
private val Muted = Color(0xFF77766F)
private val Red = Color(0xFFE34B32)
private val Rule = Color(0xFFE3DED3)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SceneApp() }
    }
}

@Composable
private fun SceneApp() {
    var tab by remember { mutableIntStateOf(0) }
    MaterialTheme(colorScheme = lightColorScheme(primary = Red, background = Paper, surface = Paper, onSurface = Ink)) {
        Scaffold(containerColor = Paper, bottomBar = {
            NavigationBar(containerColor = Paper, tonalElevation = 0.dp) {
                NavigationBarItem(selected = tab == 0, onClick = { tab = 0 }, icon = { Icon(Icons.Outlined.Explore, null) }, label = { Text("Discover") })
                NavigationBarItem(selected = tab == 1, onClick = { tab = 1 }, icon = { Icon(Icons.Outlined.Map, null) }, label = { Text("Around") })
                NavigationBarItem(selected = tab == 2, onClick = { tab = 2 }, icon = { Icon(Icons.Outlined.AddAPhoto, null) }, label = { Text("Capture") })
                NavigationBarItem(selected = tab == 3, onClick = { tab = 3 }, icon = { Icon(Icons.Outlined.PersonOutline, null) }, label = { Text("You") })
            }
        }) { padding ->
            when (tab) {
                0 -> DiscoverScreen(Modifier.padding(padding))
                1 -> PlaceholderScreen("Around you", "Nearby missions will appear when you choose to share your location.")
                2 -> PlaceholderScreen("Make a discovery", "Choose a mission, capture an original photo and share what you found.")
                else -> PlaceholderScreen("Your field notes", "Your completed missions, saved discoveries and reputation live here.")
            }
        }
    }
}

@Composable
private fun DiscoverScreen(modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(horizontal = 22.dp, vertical = 22.dp), verticalArrangement = Arrangement.spacedBy(22.dp)) {
        item {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("SCENE", fontSize = 13.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
                    Text("COIMBATORE  /  FIELD GUIDE 01", fontSize = 9.sp, color = Muted, letterSpacing = 1.2.sp)
                }
                Box(Modifier.size(38.dp).clip(CircleShape).background(Ink), contentAlignment = Alignment.Center) { Text("S", color = Paper, fontWeight = FontWeight.Bold) }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("THE CITY IS\nFULL OF STORIES.", fontSize = 38.sp, lineHeight = 39.sp, fontWeight = FontWeight.Black, letterSpacing = (-2).sp, color = Ink)
                Text("Go find one. Your next little adventure starts nearby.", fontSize = 14.sp, color = Muted)
            }
        }
        item {
            Column(Modifier.fillMaxWidth().clip(RoundedCornerShape(5.dp)).background(Ink).padding(18.dp), verticalArrangement = Arrangement.spacedBy(13.dp)) {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("TODAY'S FIELD NOTE", color = Color(0xFFFFB7A8), fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.5.sp)
                    Text("01 / 03", color = Color.White.copy(alpha = .6f), fontSize = 10.sp)
                }
                Text("Find the ₹50\nfood legend.", color = Color.White, fontSize = 30.sp, lineHeight = 31.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = (-1).sp)
                Text("A tiny-budget taste test. Find a local favourite, show what you ordered, and tell the city why it deserves the hype.", color = Color.White.copy(alpha = .78f), fontSize = 13.sp, lineHeight = 19.sp)
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("15–30 MIN  ·  FOOD  ·  EASY", color = Color.White.copy(alpha = .7f), fontSize = 9.sp)
                    Text("TAKE THE CHALLENGE  ↗", Modifier.clip(RoundedCornerShape(3.dp)).background(Red).clickable { }.padding(horizontal = 11.dp, vertical = 10.dp), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("OUT THERE", color = Red, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                Text("Missions worth the detour", fontSize = 22.sp, fontWeight = FontWeight.Bold, letterSpacing = (-.6).sp)
            }
        }
        item { MissionRow("01", "The sign nobody notices", "Find the funniest shop sign on your street.", "PHOTO HUNT  ·  10 MIN") }
        item { MissionRow("02", "A quiet corner", "Show a peaceful spot hiding in plain sight.", "HIDDEN PLACE  ·  20 MIN") }
        item { MissionRow("03", "Local legend", "Find a neighbourhood favourite and tell its story.", "FOOD QUEST  ·  30 MIN") }
        item { Text("GOOD DISCOVERIES MAKE GOOD STORIES.", fontSize = 9.sp, color = Muted, letterSpacing = 1.3.sp) }
    }
}

@Composable
private fun MissionRow(number: String, title: String, description: String, meta: String) {
    Column(Modifier.fillMaxWidth().clickable { }) {
        Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(13.dp)) {
            Text(number, color = Red, fontSize = 11.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp))
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, letterSpacing = (-.4).sp)
                Text(description, color = Muted, fontSize = 12.sp, lineHeight = 17.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
                Text(meta, color = Muted, fontSize = 9.sp, letterSpacing = .8.sp)
            }
            Text("↗", color = Red, fontSize = 20.sp)
        }
        Spacer(Modifier.height(14.dp))
        HorizontalDivider(color = Rule)
    }
}

@Composable
private fun PlaceholderScreen(title: String, description: String) {
    Column(Modifier.fillMaxSize().background(Paper).padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("SCENE", fontSize = 12.sp, fontWeight = FontWeight.Black, letterSpacing = 3.sp)
        Spacer(Modifier.height(24.dp))
        Text(title.uppercase(), fontSize = 34.sp, lineHeight = 36.sp, fontWeight = FontWeight.Black, letterSpacing = (-1.4).sp)
        Text(description, fontSize = 14.sp, color = Muted, lineHeight = 21.sp)
    }
}
