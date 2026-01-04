# 🎬 MoviesApp

A modern Android application built to demonstrate **Clean Architecture**, **Jetpack Compose**, and **Offline-First** principles. The app allows users to discover trending movies, search for titles, view detailed information, and save their favorites for offline viewing.

## 📱 Features

* **Discover:** Browse "Trending" and "Now Playing" movies with infinite scrolling.
* **Search:** Real-time search with debounce and **Offline Fallback** (searches local DB if network fails).
* **Offline First:** All browsed and searched data is cached locally using Room.
* **Bookmarks:** Save movies to your "Watchlist" (works completely offline).
* **Deep Linking:** Share movies via URL (`https://www.moviedbapp.com/movie/{id}`) that opens directly in the app.
* **Modern UI:** Built entirely with Jetpack Compose and Material3.

## 🛠 Tech Stack

* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material3)
* **Architecture:** Clean Architecture (Presentation, Domain, Data layers) + MVVM
* **Dependency Injection:** [Hilt](https://dagger.dev/hilt/)
* **Network:** [Retrofit](https://square.github.io/retrofit/) + [OkHttp](https://square.github.io/okhttp/)
* **Local Database:** [Room](https://developer.android.com/training/data-storage/room)
* **Concurrency:** Coroutines + Flow
* **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
* **Navigation:** Compose Navigation

## 🏗 Architecture

The project follows the principles of **Clean Architecture** to ensure separation of concerns and testability:

1.  **Presentation Layer:** ViewModels and Compose UI.
2.  **Domain Layer:** UseCases and Repository Interfaces (Pure Kotlin, no Android dependencies).
3.  **Data Layer:** Repository Implementations, API (Retrofit), and Database (Room).

## 🚀 Setup & Installation

To run this project, you will need to configure the TMDB API key.

### 1. Get an API Key
1.  Sign up for an account at [The Movie Database (TMDB)](https://www.themoviedb.org/).
2.  Navigate to **Settings** -> **API** to generate your unique API Key.

### 2. Configure the Project
1.  Clone this repository.
2.  Open the project in Android Studio.
3.  Open the `local.properties` file in the root directory (this file is ignored by Git).
4.  Add your API key in the following format:

```properties
sdk.dir=...
tmdb.api.key=YOUR_ACTUAL_API_KEY_HERE
