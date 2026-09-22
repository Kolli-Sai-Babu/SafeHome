# SafeSpeak — Offline-First AI Emergency Assistant
### Created for iQOO Hackathon 2026 (Smart Living Theme)

> **Zero-Navigation + 100% Offline-First + On-Device AI Emergency Assistance**

---

## 🏆 Problem & Solution

### The Core Problem
During extreme distress, high-stress emergency scenarios, or personal safety threats, individuals often **lack the time, composure, or hand mobility to unlock their smartphone, open an app, navigate menus, and send an emergency alert.** Furthermore, during disaster scenarios or network outages, cloud-dependent AI applications fail completely.

### The SafeSpeak Solution
**SafeSpeak** eliminates navigation friction by introducing a **zero-touch emergency activation workflow**. Spoken phrases like `"SafeSpeak, I need help!"` or `"I am in danger"` are processed instantly by a **lightweight, on-device NLP intent classifier**. Within milliseconds, SafeSpeak enters **Safe Mode**, acquires real-time GPS hardware coordinates, compiles the local emergency profile, provides tactile haptic response, and logs the incident locally—**operating 100% without an internet connection.**

---

## 📱 Tech Stack & Target Platform

- **Target Device**: Optimized for **iQOO Android Smartphones** (Android 8.0 / API 26 through Android 15 / API 35)
- **UI Framework**: **Jetpack Compose** with Material 3 iQOO Dark Glassmorphism aesthetics
- **On-Device AI Engine**: Local Term-Frequency & Levenshtein Vector Classifier (Zero cloud API dependency)
- **Speech Engine**: Offline Android `SpeechRecognizer` (`EXTRA_PREFER_OFFLINE = true`) + Embedded Hackathon Preset Simulator
- **Database**: **Android Room SQLite** (Encrypted local persistence)
- **Hardware Integration**: Android Location Services (Fused + Direct GPS Hardware), `VibratorManager` (iQOO tactile engine), Android Text-To-Speech (TTS)
- **Architecture**: Clean MVVM + Repository Pattern with Kotlin Coroutines & Flow

---

## 📂 Complete Project Structure

```
SafeHome/
├── app/
│   ├── build.gradle.kts
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/iqoo/safespeak/
│           │   ├── SafeSpeakApplication.kt
│           │   ├── MainActivity.kt
│           │   ├── ai/
│           │   │   ├── IntentClassificationResult.kt
│           │   │   ├── OfflineIntentClassifier.kt
│           │   │   └── VoiceTriggerListener.kt
│           │   ├── data/
│           │   │   ├── local/
│           │   │   │   ├── AppDatabase.kt
│           │   │   │   ├── EmergencyDao.kt
│           │   │   │   ├── EmergencyEvent.kt
│           │   │   │   └── UserProfile.kt
│           │   │   └── repository/
│           │   │       └── SafeSpeakRepository.kt
│           │   ├── services/
│           │   │   ├── HapticFeedbackManager.kt
│           │   │   ├── OfflineLocationProvider.kt
│           │   │   └── VoiceFeedbackManager.kt
│           │   ├── ui/
│           │   │   ├── components/
│           │   │   │   └── DemoModeBar.kt
│           │   │   ├── screens/
│           │   │   │   ├── HistoryScreen.kt
│           │   │   │   ├── MainDashboardScreen.kt
│           │   │   │   ├── ProfileScreen.kt
│           │   │   │   ├── SafeModeScreen.kt
│           │   │   │   └── SettingsPrivacyScreen.kt
│           │   │   └── theme/
│           │   │       └── Theme.kt
│           │   └── viewmodel/
│           │       └── MainViewModel.kt
│           └── res/
│               ├── values/
│               │   ├── strings.xml
│               │   └── styles.xml
│               └── xml/
│                   ├── backup_rules.xml
│                   └── data_extraction_rules.xml
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       └── gradle-wrapper.properties
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── gradlew.bat
└── README.md
```

---

## 🤖 How the On-Device AI Works

SafeSpeak features an **On-Device NLP Intent Engine** (`OfflineIntentClassifier.kt`) designed specifically to execute locally on low-power mobile microprocessors with zero latency and zero privacy risk.

1. **Utterance Normalization**: Converts raw spoken input to lowercase token vectors.
2. **Weighted Lexicon Scoring**: Evaluates high-priority safety keywords (`safespeak`, `emergency`, `danger`, `sos`, `attack`, `help`, `save`, `following`).
3. **Fuzzy N-Gram & Levenshtein Vector Distance**: Compares the input utterance against known emergency phrase vectors using string similarity metrics:
   $$\text{Similarity}(S_1, S_2) = 1.0 - \frac{\text{LevenshteinDistance}(S_1, S_2)}{\max(\text{length}(S_1), \text{length}(S_2))}$$
4. **Intent Confidence Score**: Returns a normalized confidence score between `0.0` and `1.0`. Inputs scoring `≥ 0.55` trigger Emergency Safe Mode immediately.

---

## 🛡️ Privacy Guarantee

> **“SafeSpeak processes emergency voice commands locally on the device whenever possible.”**

- **Zero Cloud Transmission**: Speech recordings never leave the RAM of your smartphone.
- **No Third-Party APIs**: Does not use OpenAI, Gemini, Groq, Firebase, or external web services.
- **Local SQLite Encrypted Storage**: Profiles and incident history remain on local storage inside Room DB.

---

## 🛠️ Build & Installation Guide

### Prerequisites
- Android Studio Jellyfish (2024.1.1) or newer
- JDK 17 / JDK 21
- Android SDK Platform 34 (Android 14) or newer
- An **iQOO Smartphone** (or Android 8.0+ device/emulator)

### 1. Build APK via Command Line
Open PowerShell / Terminal in the project root directory:
```bash
./gradlew assembleDebug
```
The compiled APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

### 2. Install on iQOO Smartphone via ADB
Enable **Developer Options** and **USB Debugging** on your iQOO device, then execute:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 📡 Complete Offline Test Protocol

To demonstrate 100% offline compliance for hackathon judges:

1. **Disconnect Connectivity**:
   - Turn OFF **Wi-Fi**.
   - Turn OFF **Mobile Data**.
   - Enable **Airplane Mode**.
2. **Launch SafeSpeak**:
   - Verify green badges: `🟢 OFFLINE READY`, `AI: ON-DEVICE`, `NETWORK: OFFLINE`.
3. **Run Voice Simulation / Live Listener**:
   - Tap **“TEST VOICE TRIGGER”** or use the **HACKATHON DEMO MODE** preset `"SafeSpeak, I need help!"`.
4. **Observe Emergency Activation**:
   - Safe Mode activates instantly.
   - 10-second false alarm countdown timer begins.
   - iQOO tactile vibration pulse triggers.
   - GPS hardware latches location (Latitude & Longitude displayed).
   - Saved local profile & emergency contact are presented.
5. **Inspect Local History**:
   - Confirm or Cancel alert.
   - Open **Emergency History** to inspect the newly recorded Room DB log.

---

## 🎬 3-Minute Hackathon Demo Script

- **[0:00 - 0:30] The Challenge & Concept**:
  *"Judges, imagine being in danger or under extreme stress. You don't have time to unlock your phone, search for an app, and type a message. Cloud apps fail when cell towers are overloaded. Meet SafeSpeak—the offline-first AI emergency assistant built for iQOO devices."*

- **[0:30 - 1:15] Demonstrating Offline Readiness**:
  *"Notice our device is currently in Airplane Mode—Wi-Fi and Cellular are completely disabled. SafeSpeak is showing 'OFFLINE READY' and 'AI: ON-DEVICE'."*

- **[1:15 - 2:15] Voice Trigger & Safe Mode Execution**:
  *"Now watch what happens when I trigger our predefined voice command: 'SafeSpeak, I need help!'. Our on-device AI intent classifier recognizes the emergency in milliseconds. Safe Mode activates instantly with iQOO haptic feedback, latches real-time GPS coordinates, loads local medical data, and starts a 10-second false activation protection timer."*

- **[2:15 - 3:00] Verification & Privacy**:
  *"We tap Confirm. The incident is logged directly into Android Room DB. Zero data leaves the device. Zero cloud dependency. SafeSpeak delivers zero-navigation smart safety for Smart Living."*

---

## ⚠️ Known Limitations & Future Improvements

### Current Prototype Limitations
- Offline speech recognition depends on Android device offline language package availability.
- GPS hardware fix time indoors can take a few seconds longer without assisted network positioning.

### Future Roadmap
- Integration with iQOO Monster Touch physical side buttons for stealth hardware triggers.
- Quantized ONNX / TensorFlow Lite micro-LLM implementation for multi-lingual intent parsing.
- Direct SMS background dispatch via SIM card hardware once cellular link is restored.
#   S a f e H o m e  
 