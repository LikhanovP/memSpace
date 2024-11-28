package xm.space.ultimatememspace.business.repositories.memes.freememes

import xm.space.ultimatememspace.business.domain.models.memes.MemeOption
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack

interface FreeMemesRepository {

    fun getFreeMemesAtPack(pack: MemesPack): List<MemeOption>
}