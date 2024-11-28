package xm.space.ultimatememspace.presentation.screens.gameresult.models

import xm.space.ultimatememspace.business.domain.models.results.PlayerResult
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.models.BaseState

/**
 * Game result screen state
 */
data class GameResultState(
    val winnerTitle: String = String.empty(),
    val winnerGame: List<PlayerResult> = emptyList(),
    val isOwner: Boolean = false,
    val resultsByPlayers: List<PlayerResult> = emptyList(),
    val nextRoundTitle: String = String.empty(),
    override val isOverlayLoading: Boolean = false
): BaseState()