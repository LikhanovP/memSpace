package xm.space.ultimatememspace.business.repositories.practicemanager

import kotlinx.coroutines.flow.SharedFlow
import xm.space.ultimatememspace.business.domain.models.connecteduser.ConnectedUser
import xm.space.ultimatememspace.business.domain.models.memes.MemeGameState
import xm.space.ultimatememspace.business.domain.models.memes.MemeRoundVotes
import xm.space.ultimatememspace.business.domain.models.memes.PlayerMemes
import xm.space.ultimatememspace.business.domain.models.results.PlayerResult
import xm.space.ultimatememspace.business.domain.models.situation.SituationOption
import xm.space.ultimatememspace.presentation.models.result.ResultUi

interface PracticeManagerRepository {

    suspend fun observeConnectedUsers(): SharedFlow<List<ConnectedUser>>

    suspend fun addNewUser(user: ConnectedUser)

    suspend fun updateRoundCount(value: Int)

    /**
     * Initialize game process
     * returns Memes for each player
     */
    suspend fun initializeGameState(): List<PlayerMemes>

    /**
     * Initialize queue players
     * returns Current active player
     */
    suspend fun setQueueUsers(): ConnectedUser?

    /**
     * Getting list of situations
     */
    suspend fun getSituationsAtGame(): List<SituationOption>

    /*** Increase round count */
    suspend fun updateRoundOrder()

    /*** Getting round number */
    suspend fun getRoundOrder(): Int

    /**
     * Adding meme vote
     * @param userId User identifier
     * @param memeId Meme identifier
     */
    suspend fun addUserMemeChoose(userId: Int, memeId: Int): Boolean

    suspend fun allMemePicksDone(): Boolean

    /*** Getting memes votes */
    suspend fun getUsersMemeChoose(): List<MemeGameState>

    suspend fun addPickPlayer(playerWhoPickId: Int, memeId: Int): Boolean

    /*** Calculate votes at round */
    suspend fun getMemeCountVotes(): List<MemeRoundVotes>

    suspend fun getResultRound(): Pair<List<ResultUi>, List<ResultUi>>

    /*** Flag of end game */
    suspend fun isTheEnd(): Boolean

    /*** Calculate round results */
    suspend fun calculateResult(): List<PlayerResult>

    /*** New game event */
    suspend fun goNextRound(): ConnectedUser?

    /*** Getting new memes for players */
    suspend fun getNewPlayersMemes(): List<PlayerMemes>

    suspend fun recheckOnlineUsers(ids: List<Int>)

    suspend fun saveActivePlayerAtRound(id: Int)

    suspend fun getActivePlayerAtRound(): Int

    suspend fun isActiveUserInParty(id: Int) : Boolean

    suspend fun getNextUserAfterRecheck(): ConnectedUser?

    suspend fun allBestMemePicksDone(): Boolean

    suspend fun clearAllDataBeforeNewGame()

    suspend fun isPlayersEmpty(): Boolean
}