package xm.space.ultimatememspace.presentation.screens.connectedplayers.models

import xm.space.ultimatememspace.core.models.BaseState
import xm.space.ultimatememspace.presentation.models.player.PlayerUi

data class ConnectedPlayersState(
    val players: List<PlayerUi> = emptyList(),
    override val isOverlayLoading: Boolean = false
): BaseState()