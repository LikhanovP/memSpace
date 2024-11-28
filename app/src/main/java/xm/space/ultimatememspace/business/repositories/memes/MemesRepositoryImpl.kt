package xm.space.ultimatememspace.business.repositories.memes

import xm.space.ultimatememspace.business.domain.models.memes.MemeOption
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack
import xm.space.ultimatememspace.business.repositories.memes.freememes.FreeMemesRepository
import xm.space.ultimatememspace.business.repositories.memes.paidmemes.PaidMemesRepository
import xm.space.ultimatememspace.business.repositories.profile.ProfileRepository

/**
 * Implementation [MemesRepository]
 */
class MemesRepositoryImpl(
    private val profileRepository: ProfileRepository,
    private val freeMemesRepository: FreeMemesRepository,
    private val paidMemesRepository: PaidMemesRepository
) : MemesRepository {

    override suspend fun getMemes(): List<MemeOption> {
        return getAvailableMemes(groups = profileRepository.getAvailableMemesPack()).shuffled()
    }

    override suspend fun getMemesAtPack(pack: MemesPack): List<MemeOption> {
        return freeMemesRepository.getFreeMemesAtPack(pack) + paidMemesRepository.getPaidMemesAtPack(pack)
    }

    override fun getFreeMemesAtPack(pack: MemesPack) = freeMemesRepository.getFreeMemesAtPack(pack).size

    override fun getPaidMemesAtPack(pack: MemesPack) = paidMemesRepository.getPaidMemesAtPack(pack).size

    private fun getAvailableMemes(
        groups: List<MemesPack>
    ): List<MemeOption> = mutableListOf<MemeOption>().apply {
        groups.forEach { pack ->
            addAll(freeMemesRepository.getFreeMemesAtPack(pack).shuffled())
//            when (pack) {
//                is MemesPack.StarterPack -> {
//                    addAll(getStarterPack().shuffled())
//                }
//                is MemesPack.FamousPeople -> {
//                    addAll(getFamousPeople().shuffled())
//                }
//                is MemesPack.Legends -> {
//                    addAll(getLegends().shuffled())
//                }
//            }
        }
    }
}