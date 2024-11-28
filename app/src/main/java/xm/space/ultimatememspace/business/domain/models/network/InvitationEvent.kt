package xm.space.ultimatememspace.business.domain.models.network

import xm.space.ultimatememspace.core.extensions.empty

data class InvitationEvent(
    val isActive: Boolean = false,
    val ip: String = String.empty(),
    val port: String = String.empty(),
    val ownerName: String = String.empty(),
    val titleValue: String = String.empty()
)