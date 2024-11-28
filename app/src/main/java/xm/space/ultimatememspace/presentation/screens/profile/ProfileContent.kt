package xm.space.ultimatememspace.presentation.screens.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.core.uikit.components.button.OutlineMainButton
import xm.space.ultimatememspace.core.uikit.components.gridavatars.GridAvatarsComponent
import xm.space.ultimatememspace.core.uikit.components.image.ImageComponent
import xm.space.ultimatememspace.core.uikit.components.toolbar.RightNavigationToolbar
import xm.space.ultimatememspace.core.uikit.fullBottomLinks
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullTopLinks
import xm.space.ultimatememspace.presentation.screens.shop.ShopContent
import xm.space.ultimatememspace.ui.theme.GrayMain
import xm.space.ultimatememspace.ui.theme.LiteGrayMain
import xm.space.ultimatememspace.ui.theme.White

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ProfileContent(
    vm: ProfileViewModel = koinViewModel(),
    navController: NavController
) {
    vm.navController = navController
    val barState = vm.barState.collectAsState()
    val state = vm.state.collectAsState()

    val focusManager = LocalFocusManager.current
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
        confirmValueChange = {
            if (it == ModalBottomSheetValue.Hidden) {
                vm.onFullViewClose()
            }
            true
        }
    )
    val coroutineScope = rememberCoroutineScope()
    val inputValue = remember { mutableStateOf(TextFieldValue(text = state.value.currentNickname)) }
    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler { vm.onBackEvent() }

    if (state.value.isCurtainShow) {
        showBottomSheet(
            modalSheetState = modalSheetState,
            coroutineScope = coroutineScope
        )
    }

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        modifier = Modifier.fillMaxSize(),
        sheetState = modalSheetState,
        sheetContent = {
            ShopContent(
                data = state.value.shopState,
                allMemesAtPackCallback = { memesPack ->
                    vm.onAllMemesAtPackClick(memesPack = memesPack)
                }
            )
        }
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (toolbar, editNickname, avatars, proceed) = createRefs()
            RightNavigationToolbar(
                data = barState.value,
                modifier = Modifier.constrainAs(toolbar) { fullTopLinks() },
                rightIconCallback = { barState.value.rightIcon?.let { vm.onMemesShopClick() } },
                leftIconCallback = { vm.onBackEvent() }
            )
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(color = LiteGrayMain, shape = RoundedCornerShape(30))
                    .constrainAs(editNickname) {
                        fullHorizontalLinks()
                        top.linkTo(toolbar.bottom)
                    }
            ) {
                val (textField, clear) = createRefs()
                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(1.dp)
                        .constrainAs(textField) { fullTopLinks() },
                    value = inputValue.value,
                    onValueChange = { name -> inputValue.value = name },
                    textStyle = TextStyle(color = White, fontSize = 18.sp),
                    label = {
                        Text(
                            text = state.value.nickPlaceholder,
                            fontSize = 16.sp,
                            color = White,
                            fontStyle = FontStyle.Italic
                        )
                    },
                    shape = RoundedCornerShape(30),
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        backgroundColor = GrayMain,
                        cursorColor = White
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
                )
                ImageComponent(
                    modifier = Modifier
                        .width(40.dp)
                        .height(40.dp)
                        .padding(end = 8.dp)
                        .constrainAs(clear) {
                            top.linkTo(textField.top)
                            bottom.linkTo(textField.bottom)
                            end.linkTo(textField.end)
                        }
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            inputValue.value = TextFieldValue()
                        },
                    model = R.drawable.ic_oval_close,
                    contentDescription = String()
                )
            }

            GridAvatarsComponent(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .constrainAs(avatars) {
                        fullHorizontalLinks()
                        top.linkTo(editNickname.bottom)
                    },
                playerAvatars = state.value.playerAvatars,
                avatarSelectCallback = { iconId -> vm.onAvatarIconClick(iconId) },
                focusManager = focusManager
            )

            OutlineMainButton(
                modifier = Modifier
                    .constrainAs(proceed) { fullBottomLinks() }
                    .padding(bottom = 27.dp),
                actionTitle = state.value.confirmTitle,
                isEnabled = inputValue.value.text.isNotEmpty() && state.value.playerAvatars.find { it.isSelect }?.id != null,
                callback = {
                    vm.onSaveClick(
                        name = inputValue.value.text,
                        iconId = state.value.playerAvatars.first { it.isSelect }.id
                    )
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
private fun showBottomSheet(
    modalSheetState: ModalBottomSheetState,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch {
        modalSheetState.show()
    }
}