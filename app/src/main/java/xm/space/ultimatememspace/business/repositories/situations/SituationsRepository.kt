package xm.space.ultimatememspace.business.repositories.situations

import xm.space.ultimatememspace.business.domain.models.situation.SituationOption

/**
 * Data source of situations
 */
interface SituationsRepository {

    /**
     * Getting list of situations
     */
    suspend fun getQuestions(): List<SituationOption>

    suspend fun saveSituationAtRound(id: Int)

    suspend fun getSituationAtRound(): SituationOption?
}