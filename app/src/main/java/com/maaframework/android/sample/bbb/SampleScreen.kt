package com.maaframework.android.sample.bbb

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maaframework.android.host.MaaHostActions
import com.maaframework.android.host.MaaHostState
import com.maaframework.android.host.MaaHostTab
import com.maaframework.android.host.MaaHostUiConfig
import com.maaframework.android.ui.MaaHostApp

@Composable
fun MaaBbbSampleScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) { viewModel.hostActions() }
    MaaHostApp(
        state = state.toHostState(),
        actions = actions,
        config = MaaHostUiConfig(
            appName = state.manifest.displayName,
            versionName = BuildConfig.VERSION_NAME,
        ),
    )
}

private fun MainUiState.toHostState() = MaaHostState(
    activeTab = activeTab.toHostTab(),
    manifest = manifest,
    catalog = catalog,
    resourceRepository = resourceRepository,
    resourceRepositoryUpdating = resourceRepositoryUpdating,
    resourceRepositoryProgress = resourceRepositoryProgress,
    resourceRepositoryClearConfirmVisible = resourceRepositoryClearConfirmVisible,
    rootReport = rootReport,
    rootConnected = rootConnected,
    servicePing = servicePing,
    runtimeState = runtimeState,
    selectedResourceId = selectedResourceId,
    selectedTaskId = selectedTaskId,
    checkedTaskIds = checkedTaskIds,
    taskOptionSelectionsByTask = taskOptionSelectionsByTask,
    taskInputValuesByTask = taskInputValuesByTask,
    selectedPresetId = selectedPresetId,
    overrideJson = overrideJson,
    logLevel = logLevel,
    displayLogs = displayLogs,
    lastMessage = lastMessage,
    busy = busy,
)

private fun MainViewModel.hostActions() = MaaHostActions(
    selectTab = { selectTab(it.toAppTab()) },
    selectResource = ::selectResource,
    selectTask = ::selectTask,
    toggleTaskChecked = ::toggleTaskChecked,
    selectPreset = ::selectPreset,
    updateOverrideJson = ::updateOverrideJson,
    updateLogLevel = ::updateLogLevel,
    updateTaskSwitchOption = ::updateTaskSwitchOption,
    toggleTaskCheckboxOption = ::toggleTaskCheckboxOption,
    updateTaskInputValue = ::updateTaskInputValue,
    refreshResourceRepository = ::refreshResourceRepository,
    requestClearResourceRepositoryConfirmation = ::requestClearResourceRepositoryConfirmation,
    dismissClearResourceRepositoryConfirmation = ::dismissClearResourceRepositoryConfirmation,
    clearResourceRepository = ::clearResourceRepository,
    requestRootAndConnect = { requestRootAndConnect() },
    prepareRuntime = ::prepareRuntime,
    startWindowedGame = ::startWindowedGame,
    startSelectedTask = ::startSelectedTask,
    startSelectedPreset = ::startSelectedPreset,
    stopRun = ::stopRun,
    toggleDisplayPower = ::toggleDisplayPower,
    exportDiagnostics = ::exportDiagnostics,
    exportConfig = ::exportConfig,
    importConfig = ::importConfig,
    setPreviewSurface = ::setPreviewSurface,
    onPreviewTouchDown = ::onPreviewTouchDown,
    onPreviewTouchMove = ::onPreviewTouchMove,
    onPreviewTouchUp = ::onPreviewTouchUp,
)

private fun MaaBbbTab.toHostTab() = when (this) {
    MaaBbbTab.Home -> MaaHostTab.Home
    MaaBbbTab.Tasks -> MaaHostTab.Tasks
    MaaBbbTab.Settings -> MaaHostTab.Settings
    MaaBbbTab.Logs -> MaaHostTab.Logs
}

private fun MaaHostTab.toAppTab() = when (this) {
    MaaHostTab.Home -> MaaBbbTab.Home
    MaaHostTab.Tasks -> MaaBbbTab.Tasks
    MaaHostTab.Settings -> MaaBbbTab.Settings
    MaaHostTab.Logs -> MaaBbbTab.Logs
}
