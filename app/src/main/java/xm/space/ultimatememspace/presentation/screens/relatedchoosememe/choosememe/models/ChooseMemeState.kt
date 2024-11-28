package xm.space.ultimatememspace.presentation.screens.relatedchoosememe.choosememe.models

import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.models.BaseState
import xm.space.ultimatememspace.core.uikit.components.image.models.CurtainState
import xm.space.ultimatememspace.presentation.models.meme.MemeUi
import xm.space.ultimatememspace.presentation.models.situations.SituationUi

/**
 * Choose meme screen state
 * @property situation Situation data in round
 * @property memes List of memes
 * @property confirmMemeTitle Action description
 * @property memesTitle Title for list of memes
 * @property situationTitle Title for situation
 */
data class ChooseMemeState(
    val situation: SituationUi = SituationUi(),
    val memes: List<MemeUi> = emptyList(),
    val confirmMemeTitle: String = String.empty(),
    val memesTitle: String = String.empty(),
    val situationTitle: String = String.empty(),
    val curtainState: CurtainState = CurtainState(),
    override val isOverlayLoading: Boolean = false
): BaseState()