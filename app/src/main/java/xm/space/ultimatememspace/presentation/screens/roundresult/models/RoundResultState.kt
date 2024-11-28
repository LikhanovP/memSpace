package xm.space.ultimatememspace.presentation.screens.roundresult.models

import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.models.BaseState
import xm.space.ultimatememspace.core.uikit.components.image.models.CurtainState
import xm.space.ultimatememspace.presentation.models.result.ResultUi

/**
 * Round result screen state
 * @property winnersMemes List of winners at round
 * @property otherMemes List of other votes
 * @property isOwner Flag of main player
 * @property nextRoundTitle Action title
 * @property winnerTitle Title winner
 */
data class RoundResultState(
    val winnersMemes: List<ResultUi> = emptyList(),
    val otherMemes: List<ResultUi> = emptyList(),
    val isOwner: Boolean = false,
    val nextRoundTitle: String = String.empty(),
    val winnerTitle: String = String.empty(),
    val curtainState: CurtainState = CurtainState(),
    override val isOverlayLoading: Boolean = false
): BaseState()