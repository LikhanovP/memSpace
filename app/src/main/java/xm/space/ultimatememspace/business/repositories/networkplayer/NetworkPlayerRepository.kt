package xm.space.ultimatememspace.business.repositories.networkplayer

import kotlinx.coroutines.flow.SharedFlow
import xm.space.ultimatememspace.business.repositories.models.ClientSocketEvents

interface NetworkPlayerRepository {

    suspend fun observeSocketEvents(): SharedFlow<ClientSocketEvents>

    suspend fun createSocket()

    suspend fun closeSocket()

    suspend fun userProfileEvent()

    /**
     * Sending choosing situation
     * @param situationId Situation identifier
     */
    suspend fun sendSituationEvent(situationId: Int)

    /**
     * Sending chosen meme
     */
    suspend fun sendMemeEvent(memeId: Int)

    /**
     * Sending chosen best meme
     * @param playerWhoChooseId User identifier
     * @param memeId Meme identifier
     */
    suspend fun playerChooseBestMemEvent(playerWhoChooseId: Int, memeId: Int)

    suspend fun expressYourself()
}