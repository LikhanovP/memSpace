package xm.space.ultimatememspace.presentation.screens.relatedsituations.choosesituation.models

import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.models.BaseState
import xm.space.ultimatememspace.presentation.models.situations.SituationUi

/**
 * Situation screen state
 * @property situations List of situations
 * @property proceedTitle Action title
 */
data class ChooseSituationState(
    val situations: List<SituationUi> = emptyList(),
    val proceedTitle: String = String.empty(),
    override val isOverlayLoading: Boolean = false
): BaseState()