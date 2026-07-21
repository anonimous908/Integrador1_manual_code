package org.example.project.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.project.AppConfig
import org.example.project.data.network.GithubVersionChecker
import org.example.project.presentation.base.BaseViewModel

data class AppState(
    val updateAvailable: Boolean = false,
    val latestVersionUrl: String? = null
)

class AppViewModel(
    private val versionChecker: GithubVersionChecker
) : BaseViewModel() {
    private val _state = MutableStateFlow(AppState())
    val state: StateFlow<AppState> = _state.asStateFlow()

    init {
        checkForUpdates()
    }

    private fun checkForUpdates() {
        launchSafely {
            val response = versionChecker.getLatestRelease()
            if (response != null) {
                val latestVersion = response.tag_name.removePrefix("v")
                val currentVersion = AppConfig.VERSION
                
                if (latestVersion > currentVersion) {
                    _state.update { it.copy(
                        updateAvailable = true,
                        latestVersionUrl = response.html_url
                    ) }
                }
            }
        }
    }
}
