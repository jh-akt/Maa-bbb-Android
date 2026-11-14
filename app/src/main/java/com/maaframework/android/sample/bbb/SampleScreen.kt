package com.maaframework.android.sample.bbb

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.maaframework.android.host.hostActions
import com.maaframework.android.host.MaaHostUiConfig
import com.maaframework.android.ui.MaaHostApp

@Composable
fun MaaBbbSampleScreen(viewModel: MainViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val actions = remember(viewModel) { viewModel.hostActions() }
    MaaHostApp(
        state = state,
        actions = actions,
        config = MaaHostUiConfig(
            appName = state.manifest.displayName,
            versionName = BuildConfig.VERSION_NAME,
        ),
    )
}
