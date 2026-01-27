Perfect 👍
Below is a **complete, clean, copy-paste READY README.md**, tailored **exactly to your app**, including **Firebase Auth, favorites, notifications, quote sharing, pagination**, etc.

You can paste this directly into **README.md** at the root of your project.

---

# 📖 DailyQuoteApp

DailyQuoteApp is a modern Android application that delivers inspirational quotes with features like daily quote notifications, favorites, sharing, search, and category filtering.
Built using **Kotlin**, **MVVM architecture**, **Firebase Authentication**, and **WorkManager**.

---

## ✨ Features

### 🏠 Home

* Infinite scroll / pagination for quotes
* Browse quotes by category:

  * Motivation
  * Love
  * Life
  * Wisdom
  * Humor
* Search quotes by content or author
* Pull-to-refresh support
* Empty state handling

### ⭐ Favorites

* Save / remove quotes as favorites
* View all favorited quotes in a dedicated screen
* Favorites persist using local storage (SharedPreferences)
* Syncs favorite state across screens

### 📅 Quote of the Day

* Prominently displayed on Home screen
* Changes daily (local logic)
* Scheduled using **WorkManager**

### 🔔 Daily Notifications

* Local push notification for “Quote of the Day”
* User can choose preferred notification time
* Android 13+ notification permission handled

### 🔐 Authentication

* Firebase Authentication (Email/Password)
* Secure login/logout flow
* User profile screen with email display

### 📤 Sharing

* Share quotes as plain text via system share sheet
* Generate quote preview card
* Save quote card image to gallery
* Multiple background styles supported

### 🧭 Navigation

* Bottom navigation (Home, Favorites, Profile)
* Proper state handling between screens

---

## 🛠 Tech Stack

* **Language:** Kotlin
* **Architecture:** MVVM
* **UI:** XML, Material Components
* **Async:** Coroutines
* **Networking:** Retrofit + Gson
* **Auth:** Firebase Authentication
* **Storage:** SharedPreferences
* **Background Work:** WorkManager
* **Notifications:** NotificationManager
* **Image Handling:** Canvas / Bitmap
* **Min SDK:** 24
* **Target SDK:** 35

---

## 📂 Project Structure

```
com.example.dailyquoteapp
│
├── api/            # Retrofit API + DTOs
├── data/           # Models, helpers
├── repository/     # Data sources
├── viewModel/      # ViewModels (business logic)
├── adapter/        # RecyclerView adapters
├── ui/             # Activities & screens
├── utils/          # Notifications, schedulers
└── App.kt          # Application class
```

---

## 🚀 Setup Instructions

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/DailyQuoteApp.git
```

---

### 2️⃣ Open in Android Studio

* Open Android Studio
* Select **Open an Existing Project**
* Choose the project folder

---

### 3️⃣ Firebase Setup (IMPORTANT)

1. Go to **Firebase Console**
2. Create a new project
3. Add Android app:

   * Package name:

     ```
     com.example.dailyquoteapp
     ```
4. Download `google-services.json`
5. Place it inside:

   ```
   app/google-services.json
   ```
6. Enable **Email/Password Authentication**

---

### 4️⃣ Enable Required Permissions

In `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```

---

### 5️⃣ Run the App

* Connect emulator or device
* Click **Run ▶**
* Grant notification permission when prompted

---

## 🔔 Notification Notes (Android 13+)

Notifications require **runtime permission**:

```kotlin
Manifest.permission.POST_NOTIFICATIONS
```

Without this permission, scheduled notifications will run but **not appear**.

---

## 🧪 Tested On

* Android Emulator (API 30–35)
* Physical Android device
* Android 13 & Android 14

---

## 📸 Screens Implemented

* Login / Signup / Splash
* Home (Quotes Feed)
* Quote of the Day
* Favorites
* Profile
* Share Quote Bottom Sheet

---

## 📌 Future Enhancements

* Cloud sync of favorites (Firestore)
* Custom quote collections
* Quote card style selector
* Dark mode
* Offline caching (Room)
* Push notifications via Firebase Cloud Messaging

---

## 🧑‍💻 Author

**Mohammad Sahil**
B.Tech CSE
Android Developer

---

## 📜 License

This project is for **educational and learning purposes**.
You are free to modify and extend it.


