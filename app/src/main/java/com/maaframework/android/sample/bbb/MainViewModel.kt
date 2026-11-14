package com.maaframework.android.sample.bbb

import android.app.Application
import com.maaframework.android.host.MaaHostViewModel

/** Preserve existing preferences while sharing the runtime and host state machine. */
class MainViewModel(application: Application) : MaaHostViewModel(
    application,
    preferencesName = "maabbb_android_settings",
)
