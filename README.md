
#  Bandwidth IQ

Bandwidth IQ (Kotlin/Compose MVVM) is a proactive network analyzer. It performs accurate, native API speed/ping tests and acts as a System Speed Analyzer (charts/history). The UI features a real-time Speedometer and a chat-style UX. It sends crucial system notifications when network quality changes, giving actionable advice like "Time for 4K streaming!".


## 🌟 Key Features

- Accurate Native Speed Testing: Utilizes device-wide APIs (not external servers) to measure download throughput and network latency (ping) accurately and reliably.
- Real-Time Speedometer: A custom-built Jetpack Compose component (Canvas-based) provides a highly visual, analog-style speedometer for instant, smooth feedback on the current network speed.
- Proactive Guidance (Chat UX): Suggestions are delivered in a conversational, chat-style interface, offering actionable recommendations based on the current connection quality (e.g., "Good for video calls, but maybe skip 4K.").
- Timely Notifications: Critical system-level notifications are triggered only when the connection quality category changes (e.g., when the speed dramatically improves or degrades), ensuring the user is alerted only when the information is actionable.
- System Speed Analyzer: Functionality similar to Speedtest.net, including:

- Test history persistence.

- Analysis of network stability (jitter).

- Visualization of performance trends over time.
## Technologies Used

- Primary Language: Kotlin

- UI Framework: Jetpack Compose

- Asynchrony: Kotlin Coroutines & Flow

- Architecture: MVVM / Clean Architecture

- Dependency Injection: (Placeholder for Hilt/Koin)

- Data Persistence: (Placeholder for Room/Firestore)
## Authors

- [@johnmalugu](https://www.github.com/JohnMalugu)


## Badges

Add badges from somewhere like: [shields.io](https://shields.io/)

[![MIT License](https://img.shields.io/badge/License-MIT-green.svg)](https://choosealicense.com/licenses/mit/)
[![GPLv3 License](https://img.shields.io/badge/License-GPL%20v3-yellow.svg)](https://opensource.org/licenses/)
[![AGPL License](https://img.shields.io/badge/license-AGPL-blue.svg)](http://www.gnu.org/licenses/agpl-3.0)

