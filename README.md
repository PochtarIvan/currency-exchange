# Currency Exchange

Currency exchange application built with Jetpack Compose following MVVM architecture.

## Tech Stack

- Kotlin
- Jetpack Compose
- MVVM
- Coroutines
- Retrofit
- Hilt
- Material 3

## Features

- Currency exchange calculation
- Currency picker
- Loading and error overlays
- Retry support
- Input normalization
- Immutable UI state

## Architecture

The project follows a layered MVVM architecture:

- `data.remote` — API DTOs and Retrofit service
- `repository` — data mapping and business rules
- `viewmodel` — UI state management and presentation logic
- `ui` — Compose UI components

The repository layer is responsible for:
- mapping API responses to app models
- handling fallback currencies
- filtering invalid API data

The ViewModel operates only with app/domain models and exposes immutable UI state for Compose.

## Error Handling

- Exchange rates loading failures show an error overlay with retry support
- Invalid API exchange rate entries are filtered during mapping
- Currency list endpoint uses a fallback list according to the task requirements

## Future Improvements

- Periodic exchange rate refresh
- Pull-to-refresh support
- Offline cache
- Better localization support
- UI tests and unit tests

## Setup

1. Clone the repository
2. Open the project in Android Studio
3. Run the app on an emulator or physical device

## Notes

The currency list endpoint currently does not return valid data.
According to the task requirements, a temporary fallback currency list is used until the API becomes available.