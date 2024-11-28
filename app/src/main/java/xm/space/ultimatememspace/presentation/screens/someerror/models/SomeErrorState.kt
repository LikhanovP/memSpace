package xm.space.ultimatememspace.presentation.screens.someerror.models

import xm.space.ultimatememspace.core.extensions.empty

data class SomeErrorState(
    val errorTitle: String = String.empty(),
    val errorDescription: String = String.empty(),
    val proceedTitle: String = String.empty()
)