package xm.space.ultimatememspace.core.models.toolbar

import xm.space.ultimatememspace.core.extensions.empty

/**
 *
 */
data class ToolbarState(
    val leftIcon: Int? = null,
    val rightIcon: Int? = null,
    val title: String = String.empty()
)