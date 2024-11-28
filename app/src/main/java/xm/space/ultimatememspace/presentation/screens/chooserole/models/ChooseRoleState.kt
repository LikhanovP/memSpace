package xm.space.ultimatememspace.presentation.screens.chooserole.models

import xm.space.ultimatememspace.business.domain.models.network.InvitationEvent
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.presentation.models.profile.ProfileCardUi

data class ChooseRoleState(
    val ownerDescription: String = String.empty(),
    val userDescription: String = String.empty(),
    val profile: ProfileCardUi? = null,
    val isThereAlreadyGameState: InvitationEvent? = null
)