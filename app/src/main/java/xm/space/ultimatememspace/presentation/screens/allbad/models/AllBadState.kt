package xm.space.ultimatememspace.presentation.screens.allbad.models

import xm.space.ultimatememspace.core.extensions.empty

data class AllBadState(
    val errorTitle: String = String.empty(),
    val errorDescription: String = String.empty(),
    val proceedTitle: String = String.empty()
)