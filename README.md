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

That's it — the app should launch and connect to your Firebase project automatically.

---

## Release Instructions

### 6. Create a release keystore

Every app published to the Play Store must be signed with a private keystore that you own.

In Android Studio:

1. **Build → Generate Signed Bundle / APK**
2. Select **Android App Bundle (.aab)** — recommended for Play Store
3. Click **Create new…** and fill in the details:

| Field | Notes |
|---|---|
| Key store path | Choose a safe location outside the project folder |
| Key store password | Strong password — do not forget it |
| Key alias | Any name, e.g. `workspace-release` |
| Key password | Can be the same as the store password |
| Validity | 25 years or more |
| Name / Organization / Country | Your details |

> ⚠️ **Back up this keystore file immediately.** If you lose it you can never update your app on the Play Store. Store it somewhere safe (e.g. a password manager or encrypted cloud backup).

### 7. Build the release bundle

After the keystore is created (or if you already have one):

1. **Build → Generate Signed Bundle / APK → Android App Bundle**
2. Select your existing keystore file and enter the passwords
3. Choose the **release** build variant
4. Click **Finish**

The generated file will be at:
```
app/release/app-release.aab
```

### 8. Publish to Google Play

1. Open [Google Play Console](https://play.google.com/console) and sign in
2. Create a **Developer Account** if you don't have one (one-time $25 registration fee)
3. Click **Create app** and fill in:
   - App name
   - Default language
   - App or game
   - Free or paid
4. Upload `app-release.aab` under **Production → Releases → Create new release**
5. Complete the required store listing:
   - Short and full description
   - Screenshots (phone, 7-inch tablet, 10-inch tablet)
   - Feature graphic (1024 × 500 px)
   - App icon (512 × 512 px)
6. Fill out the **Content rating** questionnaire
7. Complete the **Data safety** form (declare what data the app collects)
8. Add a **Privacy policy** URL (required)
9. Submit for review — Google typically reviews within 1–3 days

### Important for future updates

Always keep these two things **identical** across all releases:

| Thing | Why |
|---|---|
| Package name: `com.workspace.manager` | Changing it makes the Play Store treat it as a completely different app |
| Keystore file + passwords | Every update must be signed with the same key — a different key will be rejected |
