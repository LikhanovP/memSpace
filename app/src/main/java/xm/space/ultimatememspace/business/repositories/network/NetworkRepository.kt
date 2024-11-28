package xm.space.ultimatememspace.business.repositories.network

import kotlinx.coroutines.flow.SharedFlow
import xm.space.ultimatememspace.business.domain.models.connecteduser.ConnectedUser
import xm.space.ultimatememspace.business.domain.models.memes.PlayerMemes
import xm.space.ultimatememspace.business.domain.models.network.InvitationEvent
import xm.space.ultimatememspace.business.domain.models.results.PlayerResult
import xm.space.ultimatememspace.business.domain.models.situation.SituationOption
import xm.space.ultimatememspace.business.repositories.models.ServerSocketEvents

/**
 * Work with sockets
 */
interface NetworkRepository {

    suspend fun observeSocketEvents(): SharedFlow<ServerSocketEvents>

    fun createServer()

    suspend fun createServerEvent(message: String)

    suspend fun removeCreateServerEvent()

    suspend fun searchServer(callback: (InvitationEvent) -> Unit)

    suspend fun removeSearchServer()

    suspend fun closeServer()

    fun getServerPort(): Int?

    /**
     * Sending memes for each players
     * @param memes List of memes by player
     */
    suspend fun memesForEachPlayer(memes: List<PlayerMemes>)

    /**
     * Sending active player
     * @param player Active player
     * @param questions List of available situations
     */
    suspend fun inActionPlayerEvent(
        player: ConnectedUser,
        questions: List<SituationOption>
    )

    /**
     * Sending chosen situation
     * @param situationId Identifier of situation
     */
    suspend fun choosesSituationEvent(situationId: Int)

    /*** Sending user votes */
    suspend fun choosesMemesEvent()

    /**
     * Sending votes at round
     */
    suspend fun memesVoteEvent()

    /**
     * Sending new memes for each players
     * @param memes List of memes by each player
     */
    suspend fun newMemesForEachPlayer(memes: List<PlayerMemes>)

    /**
     * Sending game result
     * @param playersResults List of result by each player
     */
    suspend fun gameResultEvent(playersResults: List<PlayerResult>)

    suspend fun pollingUsers()

    suspend fun continueGame()
}