# Maa-bbb Android

## 共用框架同步（0.2.0）

界面、主题、保亮／息屏恢复、Root 会话、预览触控与更新生命周期统一使用
`MaaHostActivity`。沿用原设置存储 `maabbb_android_settings`，保留任务勾选、参数和配置导入导出。
ADB 支持的任务排在前面，不支持的任务排在后面并禁止执行。

“自动更新上游资源”默认开启：应用在前台每 6 小时检查一次，任务准备、运行和停止
期间延后，失败后 30 分钟重试；首页也可手动更新。沿用下方指定的上游资源仓库及
子模块，同步成功后下次准备运行时生效。此模式不更新宿主 APK 或替换 Android
原生运行时；需要本地适配时可通过 manifest 的 `local_overlay_path` 接入框架覆盖规则。

克隆后执行 `git submodule update --init --recursive`。使用 JDK 21、Android SDK 36、
NDK 27.0.12077973 与 CMake 3.22.1 构建；GitHub CI 完成构建后提供 Debug APK。
本轮验收覆盖构建和自动化测试，未进行手机任务测试。


`Maa-bbb Android` 是 Maa_bbb 的 Android Root 宿主应用。框架代码不复制到本仓库，而是通过 Git submodule 引用 GitHub 上的 `MaaFramework-Android`。

## 项目关系

- `MaaFramework-Android/`
  - Git submodule
  - URL: `git@github.com:jh-akt/MaaFramework-Android.git`
  - `settings.gradle.kts` 会挂载 `:framework`、`:framework-host`、`:framework-ui` 和 Runtime 构建插件
  - `app/build.gradle.kts` 只保留应用配置，Runtime 下载/校验由框架约定插件完成
- `app/`
  - Maa_bbb Android 应用模块
  - 保留 Maa_bbb 品牌、主题、应用 ID、清单与薄 ViewModel 适配；四页宿主 UI 来自框架

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
./gradlew :app:assembleDebug :app:testDebugUnitTest
```

需要 JDK 21 和 Android SDK 36。

调试安装：

```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Framework 引用

这个项目在 `settings.gradle.kts` 中引用 submodule 里的框架模块：

```kotlin
include(":framework", ":framework-host", ":framework-ui")
project(":framework").projectDir = file("MaaFramework-Android/framework")
project(":framework-host").projectDir = file("MaaFramework-Android/framework-host")
project(":framework-ui").projectDir = file("MaaFramework-Android/framework-ui")
```

App 模块继续通过 Gradle 子项目依赖框架：

```kotlin
implementation(project(":framework"))
implementation(project(":framework-host"))
implementation(project(":framework-ui"))
```

## Runtime 自动获取

默认构建会按下面的优先级解析 Android runtime：

1. `local.properties` 中的 `maafwRuntimeDir`
2. submodule 内已准备好的 `MaaFramework-Android/runtime`
3. GitHub Release asset

Release zip 会使用同名 `.sha256` 文件校验后再解包。

默认 Release 配置：

```properties
maafwRuntimeRepo=jh-akt/MaaFramework-Android
maafwRuntimeTag=android-runtime-v1
maafwRuntimeAsset=maaframework-android-runtime-arm64-v8a.zip
```

默认 URL 等价于：

```text
https://github.com/jh-akt/MaaFramework-Android/releases/download/android-runtime-v1/maaframework-android-runtime-arm64-v8a.zip
```

开发机可以用 `local.properties` 覆盖（路径按自己的工作区填写）：

```properties
maafwRuntimeDir=../MaaFramework-Android/runtime
```

也可以直接指向一个本地或远程 zip：

```properties
maafwRuntimeUrl=<URI generated from the local archive>
```

本地归档先由相对路径生成正确的文件 URI，再填入 `maafwRuntimeUrl`：

```bash
python3 -c 'from pathlib import Path; print(Path("../MaaFramework-Android/dist/maaframework-android-runtime-arm64-v8a.zip").resolve().as_uri())'
```

`maafwRuntimeDir` 的相对路径以应用仓库根目录为基准。JDK 与 SDK 的本机路径仅放在环境变量或未提交的 `local.properties` 中。


如果需要重新下载同名 zip：

```bash
./gradlew :app:assembleDebug -PmaafwRuntimeRefresh=true
```

GitHub 上的 framework 仓库只跟踪 runtime 说明和工具脚本，实际 Android runtime 二进制需要通过 `MaaFramework-Android/tools/package_android_runtime.py` 打包并发布为 Release asset，或在本机用 `maafwRuntimeDir` 覆盖。

### 原生运行库与 16 KiB 页面

0.2.1 使用 MaaFramework 5.16.1 与同版本 Python 绑定。运行包版本和 SHA-256 固定在
`app/build.gradle.kts`，CI 校验 APK 内的全部 ELF（包括 Python 启动器、扩展和 JNI 库）
以及 ZIP 对齐。重建方法见 `MaaFramework-Android/docs/ANDROID_RUNTIME_16K.md`。
CPython 3.14.4 和原有 Python fallback 包保持不变；此更新不新增完整 NumPy/Pillow 支持。
资源自动更新与本地覆盖继续独立生效。尚需 16 KiB Android 设备上的运行验证。
