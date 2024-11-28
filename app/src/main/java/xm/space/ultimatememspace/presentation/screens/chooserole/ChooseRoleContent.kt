package xm.space.ultimatememspace.presentation.screens.chooserole

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.core.uikit.components.button.RoleButton
import xm.space.ultimatememspace.core.uikit.components.profile.ProfileView
import xm.space.ultimatememspace.core.uikit.fullBottomLinks
import xm.space.ultimatememspace.core.uikit.fullSizeLinks
import xm.space.ultimatememspace.core.uikit.fullTopLinks

/**
 * Screen layout for choose role
 * @param vm Screen logic
 * @param navController Navigation
 */
@Composable
fun ChooseRoleContent(
    vm: ChooseRoleViewModel = koinViewModel(),
    navController: NavController
) {
    val state = vm.state.collectAsState()
    vm.navController = navController

    var visible by remember { mutableStateOf(false) }
    visible = state.value.isThereAlreadyGameState?.isActive == true && state.value.isThereAlreadyGameState?.titleValue?.isNotEmpty() ?: false

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (roles, profile, snack) = createRefs()

        state.value.profile?.let { profileState ->
            ProfileView(
                modifier = Modifier
                    .constrainAs(profile) {
                        fullTopLinks()
                    }
                    .padding(horizontal = 16.dp)
                    .clickable { vm.onProfileClick() },
                profile = profileState
            )
        }

        ConstraintLayout(
            modifier = Modifier
                .constrainAs(roles) {
                    top.linkTo(profile.bottom)
                    fullBottomLinks()
                }
                .padding(top = 16.dp, bottom = 24.dp, start = 48.dp, end = 48.dp)
        ) {
            val (divider, ownerRole, userRole) = createRefs()
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(32.dp)
                    .constrainAs(divider) { fullSizeLinks() }
            )

            RoleButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(257.dp)
                    .constrainAs(ownerRole) {
                        fullTopLinks()
                        bottom.linkTo(divider.top)
                    },
                icon = R.drawable.ic_owner,
                description = state.value.ownerDescription,
                callback = { vm.onCreatePartyClick() }
            )
            RoleButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(257.dp)
                    .constrainAs(userRole) {
                        top.linkTo(divider.bottom)
                        fullBottomLinks()
                    },
                icon = R.drawable.ic_user,
                description = state.value.userDescription,
                callback = { vm.onPlayerClick() }
            )
        }

        AnimatedVisibility(
            visible = visible,
            modifier = Modifier
                .constrainAs(snack) { fullBottomLinks() }
                .padding(16.dp),
            enter = slideIn(
                initialOffset = {
                    IntOffset(0, it.height)
                },
                animationSpec = tween(100, easing = LinearEasing)
            ),
            exit = slideOut(
                targetOffset = {
                    IntOffset(0, it.height * 2)
                },
                animationSpec = tween(100, easing = LinearEasing)
            )
        ) {
            ExtendedFloatingActionButton(
                text = { Text(state.value.isThereAlreadyGameState?.titleValue.orEmpty()) },
                icon = { Icon(Icons.Filled.Check, contentDescription = "") },
                onClick = { vm.onConnectPartyClick() }
            )
        }
    }
}