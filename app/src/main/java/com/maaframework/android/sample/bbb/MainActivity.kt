package com.maaframework.android.sample.bbb

import com.maaframework.android.ui.MaaHostActivity

class MainActivity : MaaHostActivity() {
    override val hostVersionName: String = BuildConfig.VERSION_NAME
    override val hostPreferencesName: String = "maabbb_android_settings"
}
