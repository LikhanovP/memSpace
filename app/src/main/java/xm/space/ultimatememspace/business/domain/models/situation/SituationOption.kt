package xm.space.ultimatememspace.business.domain.models.situation

import xm.space.ultimatememspace.presentation.models.situations.SituationUi

/**
 * Data for situation
 * @property id Situation identifier
 * @property question Situation description
 */
data class SituationOption(
    val id: Int,
    val question: String
) {

    /*** Transfer [SituationOption] to [SituationUi] */
    fun toUi() = SituationUi(
        id = id,
        description = question
    )
}