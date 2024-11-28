package xm.space.ultimatememspace.xroot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject
import xm.space.ultimatememspace.core.uikit.components.background.MainBackground
import xm.space.ultimatememspace.navigation.ALL_BAD
import xm.space.ultimatememspace.navigation.ALL_MEMES_PACKS
import xm.space.ultimatememspace.navigation.CHOOSE_BEST_MEME
import xm.space.ultimatememspace.navigation.CHOOSE_MEME
import xm.space.ultimatememspace.navigation.CHOOSE_ROLE_CONTENT
import xm.space.ultimatememspace.navigation.CHOOSE_SITUATION
import xm.space.ultimatememspace.navigation.CONNECTED_PLAYERS
import xm.space.ultimatememspace.navigation.CREATING_GROUP
import xm.space.ultimatememspace.navigation.GAME_RESULT
import xm.space.ultimatememspace.navigation.ON_BOARDING
import xm.space.ultimatememspace.navigation.PROFILE_CONTENT
import xm.space.ultimatememspace.navigation.QR_SCANNER
import xm.space.ultimatememspace.navigation.ROUND_RESULT
import xm.space.ultimatememspace.navigation.SERVER_SHUTDOWN
import xm.space.ultimatememspace.navigation.SOME_ERROR
import xm.space.ultimatememspace.navigation.WAIT_BEST_MEME
import xm.space.ultimatememspace.navigation.WAIT_MEME
import xm.space.ultimatememspace.navigation.WAIT_SITUATION
import xm.space.ultimatememspace.presentation.screens.allbad.AllBadContent
import xm.space.ultimatememspace.presentation.screens.allmemespacks.AllMemesPacksContent
import xm.space.ultimatememspace.presentation.screens.relatedchoosebestmeme.choosebestmeme.ChooseBestMemeContent
import xm.space.ultimatememspace.presentation.screens.relatedchoosememe.choosememe.ChooseMemeContent
import xm.space.ultimatememspace.presentation.screens.chooserole.ChooseRoleContent
import xm.space.ultimatememspace.presentation.screens.relatedsituations.choosesituation.ChooseSituationContent
import xm.space.ultimatememspace.presentation.screens.connectedplayers.ConnectedPlayersContent
import xm.space.ultimatememspace.presentation.screens.creatinggroup.CreatingGroupContent
import xm.space.ultimatememspace.presentation.screens.gameresult.GameResultContent
import xm.space.ultimatememspace.presentation.screens.onboarding.OnBoardingScreen
import xm.space.ultimatememspace.presentation.screens.profile.ProfileContent
import xm.space.ultimatememspace.presentation.screens.qrscanner.QrScannerContent
import xm.space.ultimatememspace.presentation.screens.roundresult.RoundResultContent
import xm.space.ultimatememspace.presentation.screens.relatedchoosebestmeme.waitbestmeme.WaitBestMemeContent
import xm.space.ultimatememspace.presentation.screens.relatedsituations.waitchoosesituation.WaitChooseSituationContent
import xm.space.ultimatememspace.presentation.screens.relatedchoosememe.waitmeme.WaitMemeContent
import xm.space.ultimatememspace.presentation.screens.servershutdown.ServerShutdownContent
import xm.space.ultimatememspace.presentation.screens.someerror.SomeErrorContent
import xm.space.ultimatememspace.ui.theme.UltimateMemSpaceTheme

/**
 * Root app activity
 */
class MainActivity : ComponentActivity() {

    /*** Initial logic */
    private val vm: MainViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.isOnBoardingWasShowed.onEach { (isOnboardingShowed, isProfileEdit) ->
            setContent {
                val navController = rememberNavController()
                UltimateMemSpaceTheme {
                    MainBackground()
                    NavigationComponent(
                        navController = navController,
                        startDestination = when {
                            isOnboardingShowed == true && isProfileEdit == true -> CHOOSE_ROLE_CONTENT
                            isOnboardingShowed == true && isProfileEdit != true -> PROFILE_CONTENT
                            else -> ON_BOARDING
                        }
                    )
                }
            }
        }.launchIn(lifecycleScope)

        vm.finishState.onEach {
            if (it == true) finish()
        }.launchIn(lifecycleScope)
    }

    override fun onDestroy() {
        vm.closeAllConnections()
        super.onDestroy()
    }

    @Composable
    fun NavigationComponent(navController: NavHostController, startDestination: String) {
        NavHost(
            navController = navController,
            startDestination = startDestination,
            exitTransition = { exitFade() },
            enterTransition = { enterFade() }
        ) {
            composable(WAIT_SITUATION) { WaitChooseSituationContent(navController = navController) }
            composable(WAIT_MEME) { WaitMemeContent(navController = navController) }
            composable(WAIT_BEST_MEME) { WaitBestMemeContent(navController = navController) }
            composable(SOME_ERROR) { SomeErrorContent(navController = navController) }
            composable(ROUND_RESULT) { RoundResultContent(navController = navController) }
            composable(GAME_RESULT) { GameResultContent(navController = navController) }
            composable(CHOOSE_SITUATION) { ChooseSituationContent(navController = navController) }
            composable(CHOOSE_MEME) { ChooseMemeContent(navController = navController) }
            composable(CHOOSE_BEST_MEME) { ChooseBestMemeContent(navController = navController) }
            composable(ON_BOARDING) { OnBoardingScreen(navController = navController) }
            composable(CHOOSE_ROLE_CONTENT) { ChooseRoleContent(navController = navController) }
            composable(PROFILE_CONTENT) { ProfileContent(navController = navController) }
            composable(QR_SCANNER) { QrScannerContent(navController = navController) }
            composable(CONNECTED_PLAYERS) { ConnectedPlayersContent(navController = navController) }
            composable(CREATING_GROUP) { CreatingGroupContent(navController = navController) }
            composable(ALL_MEMES_PACKS) { AllMemesPacksContent(navController = navController) }
            composable(SERVER_SHUTDOWN) { ServerShutdownContent(navController = navController) }
            composable(ALL_BAD) { AllBadContent(navController = navController) }
        }
    }

    private fun enterFade() = fadeIn(animationSpec = tween(DURATION_ENTER_TIME))

    private fun exitFade() = fadeOut(animationSpec = tween(DURATION_EXIT_TIME))

    companion object {

        private const val DURATION_EXIT_TIME = 200

        private const val DURATION_ENTER_TIME = 1000
    }
}