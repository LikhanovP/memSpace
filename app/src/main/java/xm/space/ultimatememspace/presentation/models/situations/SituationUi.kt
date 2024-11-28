package xm.space.ultimatememspace.presentation.models.situations

import xm.space.ultimatememspace.core.extensions.empty

data class SituationUi(
    val id: Int = Int.MIN_VALUE,
    val description: String = String.empty(),
    val isSelect: Boolean = false
)