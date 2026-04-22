# Workspace Manager

A **local-first, real-time collaborative note-taking Android app** built with Kotlin + Jetpack Compose + Firebase.

---

## ✨ Features

| Feature | Details |
|---|---|
| 🌐 **Shared Workspace** | Global tile/grid view — all users see the same workspace in real-time |
| ⚡ **Real-time Sync** | Firebase Firestore listeners push updates to all connected devices with sub-second latency |
| 📝 **Multi-Note Management** | Create, edit, and delete notes; notes ordered by timestamp |
| 🔀 **Drag-to-Reorder** | Long-press any tile to drag and reorder; synced to all devices |
| 🖼️ **Image Assets** | Pick images from gallery — uploaded to Firebase Storage, appear in workspace |
| 🤙 **3-Finger Rotation** | *Finger 1* = select • *Finger 2* = rotate • *Finger 3* = show rotation HUD |
| 📴 **Offline Support** | Fully functional offline; changes sync automatically on reconnect |
| ⚠️ **Conflict Resolution** | Detects local vs remote conflicts; shows resolution dialog (Keep Local / Keep Remote) |
| 💀 **Process Death Recovery** | `SavedStateHandle` preserves draft text across OS kills and config changes |
| 🔒 **Atomic Writes** | Room `@Transaction` + Firestore batch writes prevent data corruption |

---

## 🏗️ Architecture

```
MVVM + Clean Architecture + Local-First
─────────────────────────────────────────
UI Layer         → Jetpack Compose Screens + ViewModels
Domain Layer     → Use Cases + Repository Interface + Domain Models
Data Layer       → Room (local) + Firestore/Storage (remote) + Repository Impl
```

```
app/src/main/java/com/workspace/manager/
├── data/
│   ├── local/          # Room DB, DAOs, Entities, NetworkConnectivityObserver
│   ├── mapper/         # NoteMapper, AssetMapper
│   ├── model/          # NoteDto, AssetDto (Firestore DTOs)
│   ├── remote/         # FirestoreDataSource, FirebaseStorageDataSource
│   ├── repository/     # WorkspaceRepositoryImpl (local-first logic)
│   └── sync/           # SyncWorker (WorkManager background sync)
├── di/                 # Hilt modules (Database, Firebase, Repository, Network)
├── domain/
│   ├── model/          # Note, Asset, WorkspaceItem, ConflictInfo, ConflictResolution
│   ├── repository/     # WorkspaceRepository interface
│   └── usecase/        # GetWorkspaceItems, SaveNote, UploadAsset, ReorderItems, ResolveConflict, DeleteNote
└── ui/
    ├── components/     # NoteTile, ImageAssetTile (3-finger rotation), RotationHUD, ConflictDialog
    ├── navigation/     # AppNavigation (Compose Nav)
    ├── note/           # NoteEditorScreen + NoteEditorViewModel
    ├── theme/          # Color, Theme (Material3), Type
    └── workspace/      # WorkspaceScreen + WorkspaceViewModel + WorkspaceUiState
```

---

## 🚀 Setup Instructions

### Prerequisites
- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK 17**
- **Gradle 8.6** (downloaded automatically by wrapper)
- **Firebase project** (Firestore, Storage, Authentication enabled)

### 1. Clone the project
```bash
git clone <your-repo-url>
cd WorkspaceManager
```

### 2. Set up Firebase

1. Go to [console.firebase.google.com](https://console.firebase.google.com)
2. Create a new project (or use existing)
3. Add an **Android app** with package name `com.workspace.manager`
4. Download `google-services.json` and place it in `app/`
5. In Firebase console, enable:
   - **Authentication** → Sign-in method → **Anonymous**
   - **Firestore Database** → Start in test mode (for development)
   - **Storage** → Start in test mode (for development)

### 3. Build & Run
Open the project in Android Studio and run on device/emulator.

```bash
# Or via command line (requires ANDROID_HOME set)
./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

---

## 🧪 Testing Real-time Sync

1. Run the app on **two emulators** simultaneously (connected to same Firebase project)
2. Create a note on Device 1 → it should appear on Device 2 within ~1 second
3. Disable network on Device 1 (Emulator → Settings → Cellular → Off)
4. Edit a note on Device 1 (changes saved locally with `⏳ Syncing` indicator)
5. Also edit the same note on Device 2 (online)
6. Re-enable network on Device 1 → **Conflict dialog** appears

---

## 🤙 3-Finger Rotation Gesture

1. Add an image to the workspace (FAB → image icon)
2. Place **Finger 1** on the image (selects it — blue border appears)
3. Place **Finger 2** → rotation mode activates
4. Place **Finger 3** → **Rotation HUD** overlay appears showing current angle in degrees
5. Lift fingers → rotation angle is saved and synced to all devices

---

## 🔧 Firestore Security Rules (Production)

Replace test-mode rules with these before going live:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /workspace/shared/{collection}/{docId} {
      allow read, write: if request.auth != null;
    }
  }
}
```

---

## 📦 Tech Stack

| Library | Version | Purpose |
|---|---|---|
| Kotlin | 1.9.23 | Language |
| Jetpack Compose BOM | 2024.05.00 | UI framework |
| Material3 | Latest | Design system |
| Hilt | 2.51.1 | Dependency injection |
| Room | 2.6.1 | Local SQLite database |
| Firebase BOM | 32.8.1 | Backend services |
| Coil | 2.6.0 | Image loading |
| WorkManager | 2.9.0 | Background sync |
| Coroutines | 1.7.3 | Async operations |
| Navigation Compose | 2.7.7 | Screen navigation |

---

## 📄 License

MIT — free to use, modify, and distribute.
