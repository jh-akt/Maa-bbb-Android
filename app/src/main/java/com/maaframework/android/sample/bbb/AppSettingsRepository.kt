package com.maaframework.android.sample.bbb

import android.content.Context
import com.maaframework.android.host.MaaHostSettings
import com.maaframework.android.host.MaaHostSettingsBackup
import com.maaframework.android.host.MaaHostSettingsRepository
import java.io.InputStream
import java.io.OutputStream

typealias AppSettings = MaaHostSettings
typealias AppSettingsBackup = MaaHostSettingsBackup

class AppSettingsRepository(context: Context) {
    private val delegate = MaaHostSettingsRepository(
        context = context,
        preferencesName = "maabbb_android_settings",
        projectId = "maa-bbb",
    )

    fun load() = delegate.load()
    fun saveSelectedResourceId(value: String?) = delegate.saveSelectedResourceId(value)
    fun saveSelectedTaskId(value: String?) = delegate.saveSelectedTaskId(value)
    fun saveSelectedPresetId(value: String?) = delegate.saveSelectedPresetId(value)
    fun saveCheckedTaskIds(value: Set<String>) = delegate.saveCheckedTaskIds(value)
    fun saveTaskOptionSelectionsByTask(value: Map<String, Map<String, Set<String>>>) =
        delegate.saveTaskOptionSelectionsByTask(value)
    fun saveTaskInputValuesByTask(value: Map<String, Map<String, Map<String, String>>>) =
        delegate.saveTaskInputValuesByTask(value)
    fun saveOverrideJson(value: String) = delegate.saveOverrideJson(value)
    fun saveLogLevel(value: String) = delegate.saveLogLevel(value)
    fun exportTo(outputStream: OutputStream) = delegate.exportTo(outputStream)
    fun importFrom(inputStream: InputStream) = delegate.importFrom(inputStream)
    fun replaceAll(settings: AppSettings) = delegate.replaceAll(settings)
}
