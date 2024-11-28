package xm.space.ultimatememspace.presentation.screens.relatedchoosebestmeme.choosebestmeme.models

import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.models.BaseState
import xm.space.ultimatememspace.core.uikit.components.image.models.CurtainState
import xm.space.ultimatememspace.presentation.models.meme.VoteBestMemeUi

/**
 * State of choose best meme
 */
data class ChooseBestMemeState(
    val memes: List<VoteBestMemeUi> = emptyList(),
    val confirmVoteTitle: String = String.empty(),
    val situationTitle: String = String.empty(),
    val situationDescription: String = String.empty(),
    val memesTitle: String = String.empty(),
    val curtainState: CurtainState = CurtainState(),
    override val isOverlayLoading: Boolean = false
): BaseState()