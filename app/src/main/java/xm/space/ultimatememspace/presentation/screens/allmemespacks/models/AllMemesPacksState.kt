package xm.space.ultimatememspace.presentation.screens.allmemespacks.models

import xm.space.ultimatememspace.core.uikit.components.image.models.CurtainState

data class AllMemesPacksState(
    val memesOptionsResIds: List<Int>,
    val curtainState: CurtainState = CurtainState()
)