package xm.space.ultimatememspace.presentation.screens.qrscanner

import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.repositories.settings.SettingsRepository
import xm.space.ultimatememspace.core.models.toolbar.ToolbarState
import xm.space.ultimatememspace.core.viewmodel.BaseViewModel
import xm.space.ultimatememspace.navigation.CHOOSE_ROLE_CONTENT
import xm.space.ultimatememspace.navigation.CONNECTED_PLAYERS
import xm.space.ultimatememspace.navigation.QR_SCANNER
import xm.space.ultimatememspace.presentation.screens.qrscanner.models.QrScannerState

class QrScannerViewModel : BaseViewModel() {

    val state: StateFlow<QrScannerState>
        get() = _state
    private val _state = MutableStateFlow(
        QrScannerState(
            permissionTitle = resourceProvider.getString(R.string.gen_permission),
            permissionDescription = resourceProvider.getString(R.string.gen_permission_description),
            proceedTitle = resourceProvider.getString(R.string.gen_permission_action)
        )
    )

    init {
        viewModelScope.launch {
            _barState.value = ToolbarState(title = resourceProvider.getString(R.string.gen_qr_scan))
        }
    }

    fun onBarcodeAnalyzed(destination: String, navController: NavController) = viewModelScope.launch {
        val data = destination.split("&")
        settingsRepository.setNetworkData(
            ip = data.first(),
            port = data.last()
        ).run {
            navController.navigate(CONNECTED_PLAYERS) {
                popUpTo(QR_SCANNER) {
                    inclusive = true
                }
            }
        }
    }

    fun onCloseClick() {
        navController?.navigate(CHOOSE_ROLE_CONTENT) {
            popUpTo(QR_SCANNER) { inclusive = true }
        }
    }
}