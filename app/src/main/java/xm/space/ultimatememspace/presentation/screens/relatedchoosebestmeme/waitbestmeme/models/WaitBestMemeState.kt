package xm.space.ultimatememspace.presentation.screens.relatedchoosebestmeme.waitbestmeme.models

import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.models.BaseState

/**
 * Wait best meme screen state
 */
data class WaitBestMemeState(
    val animType: String = "chooseloader.json",
    val waitTitle: String = String.empty(),
    override val isOverlayLoading: Boolean = false
): BaseState()