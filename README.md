# Workspace Manager

## Tools you need

- **Android Studio** Hedgehog (2023.1.1) or newer
- **JDK 17** (bundled with Android Studio)
- A **Firebase account** — the free **Spark plan** is enough (no paid plan required)

## Setup

### 1. Clone the repository
```bash
git clone https://github.com/Hasan525/WorkSpaceManager.git
cd WorkspaceManager
```

### 2. Create your own Firebase project

1. Go to [console.firebase.google.com](https://console.firebase.google.com) and sign in with your Google account.
2. Click **Add project** → name it anything (e.g. `workspace-manager-dev`) → finish the wizard.
3. Inside the project dashboard, click the **Android icon** to add an Android app:
   - **Package name**: `com.workspace.manager` *(must match exactly)*
   - **App nickname** and **SHA-1**: optional, leave blank
4. When prompted, **download the `google-services.json`** file — keep it handy for step 4.

### 3. Enable the two Firebase services this app needs

In your Firebase project's left-hand sidebar:

**a) Authentication**
- Go to **Build → Authentication → Get started**
- Open the **Sign-in method** tab
- Enable **Anonymous** and save

**b) Cloud Firestore**
- Go to **Build → Firestore Database → Create database**
- Pick any location close to your users
- Choose **Start in test mode** (open rules for the first 30 days — fine for development)
- Click **Enable**

> **Note**: This app does **not** use Firebase Storage. Image bytes are compressed and stored inline in Firestore documents, so the free Spark plan is sufficient. Skip the Storage setup.

### 4. Drop in your `google-services.json`

Replace the existing file at `app/google-services.json` with the one you downloaded in step 2.

```bash
# from the project root
mv ~/Downloads/google-services.json app/google-services.json
```

### 5. Build & run

Open the project in Android Studio → let Gradle sync finish → press **Run** (▶) on a device or emulator.

```

That's it — the app should launch and connect to your Firebase project automatically.
