# SCENE

Find something worth leaving home for.

SCENE is a native real-world discovery game. Android is Kotlin + Jetpack Compose; iOS is SwiftUI. No Expo, React Native, WebView shell, paid APIs, or paid SDKs.

## Repository
- android/: native Android app
- ios/: native iOS app
- supabase/: database schema, RLS and storage policies

## Free-first setup
Use a dedicated Supabase Free project. Apply the SQL migration in supabase/migrations. Never put a service-role key in a client app.

## Build status
Android CI builds a debug APK. A signed, device-installable iOS IPA requires Apple signing assets and provisioning. Simulator builds do not produce an installable IPA.

## Product principles
No fake activity counts, fake users or fabricated leaderboards. Request location only when users choose nearby discovery. Never encourage trespassing or unsafe challenges.

## Design
Warm paper, near-black ink, vermilion accent, editorial typography, photography-led storytelling. No generic gradients, glass dashboards or gamification clutter.
