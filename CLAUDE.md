# Claude Notes

This is the primary agent guidance file for this repository. Read this file before making changes.

## Project Context

`Maa-bbb Android` is the Maa_bbb Android Root host app. It uses `MaaFramework-Android` from GitHub as a Git submodule, then mounts the submodule's `framework/` directory as Gradle project `:framework`.

Submodule:

```text
MaaFramework-Android -> git@github.com:jh-akt/MaaFramework-Android.git
```

Gradle reference:

```kotlin
include(":framework")
project(":framework").projectDir = file("MaaFramework-Android/framework")
```

## Important Boundaries

- App-specific code lives in `app/`.
- Framework implementation lives in the `MaaFramework-Android` submodule; make reusable framework fixes in the framework repository, then update the submodule pointer here.
- Runtime files are read from `MaaFramework-Android/runtime/` during `:app` packaging.
- Do not duplicate framework source into this app repo outside the submodule.

## Important Files

Start here for Maa_bbb app behavior:

1. `app/src/main/java/com/maaframework/android/sample/bbb/MainViewModel.kt`
2. `app/src/main/java/com/maaframework/android/sample/bbb/SampleScreen.kt`
3. `app/src/main/java/com/maaframework/android/sample/bbb/ProjectInterfaceSupport.kt`
4. `app/src/main/java/com/maaframework/android/sample/bbb/AppSettingsRepository.kt`
5. `app/src/main/assets/maa_project_manifest.json`
6. `app/build.gradle.kts`
7. `settings.gradle.kts`

For framework/runtime behavior, inspect:

1. `MaaFramework-Android/framework/src/main/java/com/maaframework/android/session/MaaFrameworkSession.kt`
2. `MaaFramework-Android/framework/src/main/java/com/maaframework/android/session/MaaRuntimeClient.kt`
3. `MaaFramework-Android/framework/src/main/java/com/maaframework/android/root/RootRuntimeService.kt`
4. `MaaFramework-Android/framework/src/main/java/com/maaframework/android/runtime/RuntimeBootstrapper.kt`
5. `MaaFramework-Android/framework/src/main/java/com/maaframework/android/preview/VirtualDisplayManager.kt`

## Build Notes

Known working command:

```bash
JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home" \
PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH" \
ANDROID_SDK_ROOT="$HOME/Library/Android/sdk" \
ANDROID_HOME="$HOME/Library/Android/sdk" \
./gradlew :app:assembleDebug
```

If the framework submodule is missing:

```bash
git submodule update --init --recursive
```

Runtime binaries are not fully tracked in git. For a complete device run, prepare the Android runtime files under `MaaFramework-Android/runtime/` before packaging.

## Device Notes

- Target environment: Android 11+, `arm64-v8a`.
- The app process needs executable `su`; `adb root` alone is not enough.
- Physical device `382b528f` has previously been verified with app root authorization.
- Before long device sessions:

```bash
adb -s 382b528f shell svc power stayon usb
adb -s 382b528f shell settings put system screen_off_timeout 2147483647
adb -s 382b528f shell input keyevent 224
```
