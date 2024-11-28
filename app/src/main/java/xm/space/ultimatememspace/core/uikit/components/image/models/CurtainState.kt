package xm.space.ultimatememspace.core.uikit.components.image.models

data class CurtainState(
    val fullViewResId: Pair<Int?, Boolean> = null to false,
    val curtainCloseTitle: String? = null,
    val curtainAcceptTitle: String? = null,
    val dotWidth: Int = 12,
    val dotSpace: Int = 12,
)