package xm.space.ultimatememspace.presentation.screens.creatinggroup.models

import android.graphics.Bitmap
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.models.BaseState
import xm.space.ultimatememspace.presentation.models.player.PlayerUi
import xm.space.ultimatememspace.presentation.screens.shop.models.ShopState

/**
 * State of creating group
 */
data class CreatingGroupState(
    val players: List<PlayerUi> = emptyList(),
    val gameStartTitle: String = String.empty(),
    val makeSureTitle: String = String.empty(),
    val connectedTitle: String = String.empty(),
    val shopData: ShopState = ShopState(),
    val qrWifi: Bitmap? = null,
    val isCurtainShow: Boolean = false,
    val roundCountTitle: String = String.empty(),
    override val isOverlayLoading: Boolean = false
): BaseState()