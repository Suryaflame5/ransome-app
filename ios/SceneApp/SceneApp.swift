import SwiftUI

@main
struct SceneApp: App {
    var body: some Scene {
        WindowGroup { SceneRootView().preferredColorScheme(.light) }
    }
}

private enum Palette {
    static let paper = Color(red: 0.96, green: 0.95, blue: 0.92)
    static let ink = Color(red: 0.09, green: 0.09, blue: 0.08)
    static let muted = Color(red: 0.47, green: 0.46, blue: 0.43)
    static let red = Color(red: 0.89, green: 0.29, blue: 0.20)
    static let rule = Color(red: 0.88, green: 0.86, blue: 0.81)
}

struct SceneRootView: View {
    @State private var selected = 0
    var body: some View {
        TabView(selection: $selected) {
            DiscoverView().tabItem { Label("Discover", systemImage: "safari") }.tag(0)
            EmptySceneView(title: "Around you", message: "Nearby missions appear when you choose to share your location.").tabItem { Label("Around", systemImage: "map") }.tag(1)
            EmptySceneView(title: "Make a discovery", message: "Choose a mission, capture an original photo and share what you found.").tabItem { Label("Capture", systemImage: "camera") }.tag(2)
            EmptySceneView(title: "Your field notes", message: "Your completed missions, saved discoveries and reputation live here.").tabItem { Label("You", systemImage: "person.crop.circle") }.tag(3)
        }.tint(Palette.red)
    }
}

struct DiscoverView: View {
    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 24) {
                HStack {
                    VStack(alignment: .leading, spacing: 4) {
                        Text("SCENE").font(.system(size: 13, weight: .black)).tracking(3)
                        Text("COIMBATORE  /  FIELD GUIDE 01").font(.system(size: 9, weight: .medium)).tracking(1.2).foregroundStyle(Palette.muted)
                    }
                    Spacer()
                    Text("S").font(.system(size: 15, weight: .bold)).foregroundStyle(Palette.paper).frame(width: 38, height: 38).background(Palette.ink, in: Circle())
                }
                VStack(alignment: .leading, spacing: 8) {
                    Text("THE CITY IS\nFULL OF STORIES.").font(.system(size: 39, weight: .black, design: .rounded)).tracking(-2).lineSpacing(-2)
                    Text("Go find one. Your next little adventure starts nearby.").font(.system(size: 14)).foregroundStyle(Palette.muted)
                }
                VStack(alignment: .leading, spacing: 14) {
                    HStack {
                        Text("TODAY'S FIELD NOTE").font(.system(size: 10, weight: .bold)).tracking(1.6).foregroundStyle(Color(red: 1, green: 0.72, blue: 0.66))
                        Spacer()
                        Text("01 / 03").font(.system(size: 10)).foregroundStyle(.white.opacity(0.6))
                    }
                    Text("Find the ₹50\nfood legend.").font(.system(size: 30, weight: .heavy, design: .rounded)).tracking(-1).foregroundStyle(.white)
                    Text("A tiny-budget taste test. Find a local favourite, show what you ordered, and tell the city why it deserves the hype.").font(.system(size: 13)).lineSpacing(4).foregroundStyle(.white.opacity(0.78))
                    HStack {
                        Text("15–30 MIN  ·  FOOD  ·  EASY").font(.system(size: 9, weight: .semibold)).foregroundStyle(.white.opacity(0.7))
                        Spacer()
                        Text("TAKE THE CHALLENGE  ↗").font(.system(size: 9, weight: .bold)).padding(.horizontal, 11).padding(.vertical, 10).background(Palette.red, in: RoundedRectangle(cornerRadius: 3))
                    }
                }.padding(18).background(Palette.ink, in: RoundedRectangle(cornerRadius: 5))
                VStack(alignment: .leading, spacing: 5) {
                    Text("OUT THERE").font(.system(size: 10, weight: .bold)).tracking(2).foregroundStyle(Palette.red)
                    Text("Missions worth the detour").font(.system(size: 22, weight: .bold, design: .rounded)).tracking(-0.7)
                }
                MissionLine(number: "01", title: "The sign nobody notices", detail: "Find the funniest shop sign on your street.", meta: "PHOTO HUNT  ·  10 MIN")
                MissionLine(number: "02", title: "A quiet corner", detail: "Show a peaceful spot hiding in plain sight.", meta: "HIDDEN PLACE  ·  20 MIN")
                MissionLine(number: "03", title: "Local legend", detail: "Find a neighbourhood favourite and tell its story.", meta: "FOOD QUEST  ·  30 MIN")
                Text("GOOD DISCOVERIES MAKE GOOD STORIES.").font(.system(size: 9, weight: .medium)).tracking(1.3).foregroundStyle(Palette.muted)
            }.padding(.horizontal, 22).padding(.top, 20).padding(.bottom, 30)
        }.background(Palette.paper.ignoresSafeArea())
    }
}

struct MissionLine: View {
    let number: String
    let title: String
    let detail: String
    let meta: String
    var body: some View {
        VStack(spacing: 13) {
            HStack(alignment: .top, spacing: 13) {
                Text(number).font(.system(size: 11, weight: .bold)).foregroundStyle(Palette.red).padding(.top, 4)
                VStack(alignment: .leading, spacing: 5) {
                    Text(title).font(.system(size: 18, weight: .bold, design: .rounded)).tracking(-0.5)
                    Text(detail).font(.system(size: 12)).lineSpacing(3).foregroundStyle(Palette.muted)
                    Text(meta).font(.system(size: 9, weight: .medium)).tracking(0.8).foregroundStyle(Palette.muted)
                }
                Spacer()
                Image(systemName: "arrow.up.right").foregroundStyle(Palette.red)
            }
            Rectangle().fill(Palette.rule).frame(height: 1)
        }
    }
}

struct EmptySceneView: View {
    let title: String
    let message: String
    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("SCENE").font(.system(size: 12, weight: .black)).tracking(3)
            Spacer().frame(height: 28)
            Text(title.uppercased()).font(.system(size: 34, weight: .black, design: .rounded)).tracking(-1.5)
            Text(message).font(.system(size: 14)).foregroundStyle(Palette.muted)
            Spacer()
        }.padding(24).frame(maxWidth: .infinity, alignment: .leading).background(Palette.paper.ignoresSafeArea())
    }
}
