package xm.space.ultimatememspace.core.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import xm.space.ultimatememspace.presentation.screens.allbad.AllBadViewModel
import xm.space.ultimatememspace.presentation.screens.allmemespacks.AllMemesPacksViewModel
import xm.space.ultimatememspace.presentation.screens.relatedchoosebestmeme.choosebestmeme.ChooseBestMemeViewModel
import xm.space.ultimatememspace.presentation.screens.relatedchoosememe.choosememe.ChooseMemeViewModel
import xm.space.ultimatememspace.presentation.screens.chooserole.ChooseRoleViewModel
import xm.space.ultimatememspace.presentation.screens.relatedsituations.choosesituation.ChooseSituationViewModel
import xm.space.ultimatememspace.presentation.screens.connectedplayers.ConnectedPlayersViewModel
import xm.space.ultimatememspace.presentation.screens.creatinggroup.CreatingGroupViewModel
import xm.space.ultimatememspace.presentation.screens.gameresult.GameResultViewModel
import xm.space.ultimatememspace.presentation.screens.onboarding.OnBoardingViewModel
import xm.space.ultimatememspace.presentation.screens.profile.ProfileViewModel
import xm.space.ultimatememspace.presentation.screens.qrscanner.QrScannerViewModel
import xm.space.ultimatememspace.presentation.screens.roundresult.RoundResultViewModel
import xm.space.ultimatememspace.presentation.screens.someerror.SomeErrorViewModel
import xm.space.ultimatememspace.presentation.screens.relatedchoosebestmeme.waitbestmeme.WaitBestMemeViewModel
import xm.space.ultimatememspace.presentation.screens.relatedsituations.waitchoosesituation.WaitChooseSituationViewModel
import xm.space.ultimatememspace.presentation.screens.relatedchoosememe.waitmeme.WaitMemeViewModel
import xm.space.ultimatememspace.presentation.screens.servershutdown.ServerShutdownViewModel
import xm.space.ultimatememspace.xroot.MainViewModel

val viewModelsModule = module {
    viewModelOf(::ChooseBestMemeViewModel)
    viewModelOf(::ChooseMemeViewModel)
    viewModelOf(::ChooseSituationViewModel)
    viewModelOf(::GameResultViewModel)
    viewModelOf(::OnBoardingViewModel)
    viewModelOf(::RoundResultViewModel)
    viewModelOf(::SomeErrorViewModel)
    viewModelOf(::WaitBestMemeViewModel)
    viewModelOf(::ChooseRoleViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::QrScannerViewModel)
    viewModelOf(::ConnectedPlayersViewModel)
    viewModelOf(::CreatingGroupViewModel)
    viewModelOf(::WaitChooseSituationViewModel)
    viewModelOf(::WaitMemeViewModel)
    viewModelOf(::AllMemesPacksViewModel)
    viewModelOf(::ServerShutdownViewModel)
    viewModelOf(::AllBadViewModel)
}