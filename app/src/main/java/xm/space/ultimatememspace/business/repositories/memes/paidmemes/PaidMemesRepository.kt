package xm.space.ultimatememspace.business.repositories.memes.paidmemes

import xm.space.ultimatememspace.business.domain.models.memes.MemeOption
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack

interface PaidMemesRepository {

    fun getPaidMemesAtPack(pack: MemesPack): List<MemeOption>
}