package xm.space.ultimatememspace.presentation.screens.qrscanner.models

import xm.space.ultimatememspace.core.extensions.empty

data class QrScannerState(
    val permissionTitle: String = String.empty(),
    val permissionDescription: String = String.empty(),
    val proceedTitle: String = String.empty()
)