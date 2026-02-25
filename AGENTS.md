# wol-power-button

Very simple Android application for sending a Wake-on-LAN request using
a button or a custom app launcher.

## Testing

This project will not have tests.

## Build Commands

```bash
# Build debug APK
make debug-build
```

Important: Never run grddle or gradlew commands directly. Containers
are use for this. Agents will request the user to do it in their behalf.

## Project Structure

- **app/build.gradle** - Android app module configuration
- **app/src/main/** - Source code and resources
- **Makefile** - Containerized build targets using Podman/Docker
- **Containerfile** - Container with the build environment.

## Technology

- Gradle 8.6
- Android SDK 33/34
- Java 17

## Notes

- Uses Android Gradle Plugin 8.3.1
- Minimum SDK: 21 (Android 5.0)
- Target SDK: 33 (Android 13)
- Doesn't use AndroidX.
- In strictly a classic Java (not Kotlin) app using Android Views.
