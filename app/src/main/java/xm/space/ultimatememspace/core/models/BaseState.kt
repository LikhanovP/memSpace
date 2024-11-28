package xm.space.ultimatememspace.core.models

import xm.space.ultimatememspace.R

abstract class BaseState(
    open val isOverlayLoading: Boolean = false,
    val overlayTitleResId: Int = R.string.gen_wait_connected_users
)