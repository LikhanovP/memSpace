package xm.space.ultimatememspace.business.repositories.shop

import android.content.SharedPreferences
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack
import xm.space.ultimatememspace.business.repositories.memes.MemesRepository
import xm.space.ultimatememspace.business.repositories.profile.ProfileRepository
import xm.space.ultimatememspace.core.providers.ResourceProvider
import xm.space.ultimatememspace.presentation.models.meme.MemesOptionUi
import xm.space.ultimatememspace.business.repositories.getValue
import xm.space.ultimatememspace.business.repositories.setValue
import xm.space.ultimatememspace.core.extensions.empty

class ShopRepositoryImpl(
    preferences: SharedPreferences,
    private val resourceProvider: ResourceProvider,
    private val memesRepository: MemesRepository,
    private val profileRepository: ProfileRepository
) : ShopRepository {

    private var activePack: Int by preferences

    override suspend fun getShopPack(): List<MemesOptionUi> {
        return MemesPack.getPackForShop(
            resourceProvider = resourceProvider,
            memesRepository = memesRepository,
            availablePacks = profileRepository.getAvailableMemesPack()
        )
    }

    override suspend fun getActivePack(): List<Int> {
        return MemesPack.getPackById(activePack)?.let { pack ->
            memesRepository.getMemesAtPack(pack = pack).map { it.memeResId }
        } ?: emptyList()
    }

    override suspend fun setActivePack(memesPack: MemesPack) {
        activePack = memesPack.packId
    }

    override fun getActivePackName(): String {
        return MemesPack.getPackById(activePack)
            ?.shopCard(resourceProvider, memesRepository)?.optionTitle
            ?: String.empty()
    }
}