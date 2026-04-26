# Maa-bbb Android

`Maa-bbb Android` 是 Maa_bbb 的 Android Root 宿主应用。框架代码不复制到本仓库，而是通过 Git submodule 引用 GitHub 上的 `MaaFramework-Android`。

## 项目关系

- `MaaFramework-Android/`
  - Git submodule
  - URL: `git@github.com:jh-akt/MaaFramework-Android.git`
  - `settings.gradle.kts` 会把其中的 `framework/` 挂载成 Gradle 子项目 `:framework`
- `app/`
  - Maa_bbb Android 应用模块
  - 包含 Maa_bbb 的任务 UI、配置导入导出、虚拟屏预览和 Root Runtime 控制

## 克隆

```bash
git clone --recurse-submodules git@github.com:jh-akt/Maa-bbb-Android.git
```

如果已经普通 clone：

```bash
git submodule update --init --recursive
```

## 构建

```bash
JAVA_HOME="/opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk/Contents/Home" \
PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH" \
ANDROID_SDK_ROOT="$HOME/Library/Android/sdk" \
ANDROID_HOME="$HOME/Library/Android/sdk" \
./gradlew :app:assembleDebug
```

调试安装：

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Framework 引用

这个项目在 `settings.gradle.kts` 中引用 submodule 里的框架模块：

```kotlin
include(":framework")
project(":framework").projectDir = file("MaaFramework-Android/framework")
```

App 模块继续通过 Gradle 子项目依赖框架：

```kotlin
implementation(project(":framework"))
```

Runtime 打包目录默认指向：

```kotlin
MaaFramework-Android/runtime
```

GitHub 上的 framework 仓库只跟踪 runtime 说明和工具脚本，实际 Android runtime 二进制仍需要按框架仓库说明准备到该目录后再做完整设备运行验证。
