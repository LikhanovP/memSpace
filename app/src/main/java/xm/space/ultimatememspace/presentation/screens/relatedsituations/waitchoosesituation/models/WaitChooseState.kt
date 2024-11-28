package xm.space.ultimatememspace.presentation.screens.relatedsituations.waitchoosesituation.models

import xm.space.ultimatememspace.core.models.BaseState

/**
 * Loader situation screen state
 */
data class WaitChooseState(
    val animType: String = "chooseloader.json",
    val waitTitle: String,
    override val isOverlayLoading: Boolean = false
): BaseState()