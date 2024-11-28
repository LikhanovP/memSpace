package xm.space.ultimatememspace.business.repositories.practicemanager

import xm.space.ultimatememspace.business.domain.models.memes.MemeRoundVotes
import xm.space.ultimatememspace.business.domain.models.memevote.MemeVote
import xm.space.ultimatememspace.business.domain.models.network.NetworkEvents
import xm.space.ultimatememspace.business.domain.models.results.PlayerResult
import xm.space.ultimatememspace.business.domain.models.situation.SituationOption
import xm.space.ultimatememspace.presentation.models.meme.MemeUi
import xm.space.ultimatememspace.presentation.models.result.ResultUi

/**
 * Data source of user game state
 */
interface UserPracticeManagerRepository {

    /**
     * Saving memes starter pack
     * @param memes List of memes
     */
    suspend fun saveMemesStartPack(memes: String)

    /**
     * Saving situations pack
     * @param situations List of situations
     */
    suspend fun saveSituationsAtRound(situations: List<SituationOption>)

    /*** Getting list of situations */
    suspend fun getSituationsAtRound(): List<SituationOption>

    /*** Getting list of memes */
    suspend fun getMemesPack(): List<MemeUi>

    suspend fun saveMemesForBest(memes: List<MemeVote>, roundNumber: Int)

    suspend fun getMemesForBest(): List<MemeVote>

    suspend fun getRoundNumber(): Int

    /**
     * Saving results at round
     * @param votes Results at round by each meme
     */
    suspend fun saveResultRound(votes: List<MemeRoundVotes>)

    suspend fun getResultRound(): Pair<List<ResultUi>, List<ResultUi>>

    /*** Adding new meme to pack */
    suspend fun addNewMemeToPack(memes: String)

    /**
     * Saving game results
     * @param playersResults List of result by each player
     */
    suspend fun saveGameResult(playersResults: List<PlayerResult>)

    /*** Getting game results */
    suspend fun getGameResults(): Pair<List<PlayerResult>, List<PlayerResult>>

    /*** Remove used meme */
    suspend fun removeMeme(id: Int)
}