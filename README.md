# PulsIq — Real‑time heart rate monitoring for workouts

An Android app for real‑time heart rate tracking. PulsIq connects to a Bluetooth LE heart‑rate sensor,
records key metrics, and alerts you with sound and vibration when your heart rate goes beyond your comfort zone.

<a href="app-debug.apk?raw=1" download>You can download the APK here</a>

## Features

- Real‑time heart rate monitoring via a bluetooth.
- Configurable upper and lower heart rate limits with separate notifications: sound and vibration.
- Start/stop a session with one tap. A large heart‑rate indicator is shown during the workout.
- Each workout is recorded: duration, min/avg/max heart rate, and an heart rate trend chart.
- Activity history and a detailed card for each entry with visualization.

## Screenshots

<table>
    <tr>
        <td>
            <img src="https://github.com/Gorosheg/pulsiq/blob/master/screen1.png" width="256"/>
        </td>
        <td>
            <img src="https://github.com/Gorosheg/pulsiq/blob/master/screen2.png" width="256"/>
        </td>
        <td>
            <img src="https://github.com/Gorosheg/pulsiq/blob/master/screen3.png" width="256"/>
        </td>
    </tr>
    <tr>
        <td>Heart rate screen</td>
        <td>During a workout</td>
        <td>Workout details and heart rate chart</td>
    </tr>
    <tr>
        <td>
            <img src="https://github.com/Gorosheg/pulsiq/blob/master/screen4.png" width="256"/>
        </td>
        <td>
            <img src="https://github.com/Gorosheg/pulsiq/blob/master/screen5.png" width="256"/>
        </td>
        <td>
            <img src="https://github.com/Gorosheg/pulsiq/blob/master/screen6.png" width="256"/>
        </td>
    </tr>
    <tr>
        <td>Workouts list</td>
        <td>Sound and vibration settings</td>
        <td>Heart‑rate thresholds</td>
    </tr>
</table>

## Architecture and modules

- Multi‑module project, MVVM with a single UI state per screen.
- Layers: UI → ViewModel → Repository → Storage/Bluetooth.
- Main repository directories:
  - app — entry point, navigation, DI initialization.
  - core — base components, design system, and utilities shared across modules.
  - data — data sources and Room local storage.
  - repository — business logic and repositories (e.g., workout statistics).
  - feature — feature modules for individual screens.

## Tech stack

- Jetpack Compose — modern declarative UI.
- Koin — dependency injection.
- Room — local storage for activity history.
- Voyager — navigation between screens.
- Vico — charts and HR visualization.
- Bluetooth LE — connection to the heart‑rate sensor.
- Multi‑module + MVVM with a single state per screen.
