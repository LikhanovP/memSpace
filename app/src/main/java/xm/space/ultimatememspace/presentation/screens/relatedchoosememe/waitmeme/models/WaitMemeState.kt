package xm.space.ultimatememspace.presentation.screens.relatedchoosememe.waitmeme.models

import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.models.BaseState

/**
 * Loader meme screen state
 */
data class WaitMemeState(
    val animType: String = "chooseloader.json",
    val waitTitle: String = String.empty(),
    override val isOverlayLoading: Boolean = false
): BaseState()