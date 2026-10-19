# Mastermind

## Description
A single-player Android puzzle game inspired by classic Mastermind. Built with Kotlin and Jetpack Compose, the application allows players to customize color pools, submit guess attempts, and view real-time feedback. Player profiles, match results, and high scores are persisted locally in a database.

## Tech Stack
- Kotlin: Primary programming language

- Jetpack Compose: Declarative UI framework (Material 3)

- Architecture: MVVM pattern with State Hoisting

- Navigation Compose: Screen navigation and state routing

- Room & KSP: Local SQLite database management and persistence

- Kotlin Coroutines & Flow: Asynchronous database operations


## Key Features
- Player Profile Setup: Validate username and email, select playable color count (5–10), and upload a custom profile picture using Android Photo Picker.

- Visual Feedback via Small Circles:
  - 🟢 Green Circle: Correct color in the correct position.
  - 🟡 Yellow Circle: Correct color, but in the wrong position.
  - 🔴 Red Circle: Color is not part of the secret code.

- Local Leaderboard: Match results are automatically saved using Room and displayed on a scoreboard sorted by best score (fewest attempts).

- Custom Animations: Features infinite scaling title animations, custom screen enter/exit transitions, animated color pickers, and sequential feedback reveals.
