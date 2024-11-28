package xm.space.ultimatememspace.business.domain.models.memes

import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.repositories.memes.MemesRepository
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.providers.ResourceProvider
import xm.space.ultimatememspace.presentation.models.meme.MemesOptionUi

sealed class MemesPack {

    abstract val packId: Int

    abstract fun shopCard(
        resourceProvider: ResourceProvider,
        memesRepository: MemesRepository
    ): MemesOptionUi

    data object Avengers : MemesPack() {
        override val packId = AVENGERS
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_avengers),
            optionDesc = resourceProvider.getString(R.string.gen_avengers_desc),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Avengers)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getPaidMemesAtPack(Avengers)
            ),
            optionPrice = null,
            memesPack = Avengers
        )
    }

    data object Bale : MemesPack() {
        override val packId = BALE
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_bale),
            optionDesc = resourceProvider.getString(R.string.gen_bale_desc),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Bale)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Bale)
            ),
            optionPrice = null,
            memesPack = Bale
        )
    }

    data object Boys : MemesPack() {
        override val packId = BOYS
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_boys),
            optionDesc = resourceProvider.getString(R.string.gen_boys_desc),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Boys)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Boys)
            ),
            optionPrice = null,
            memesPack = Boys
        )
    }

    data object Chicken : MemesPack() {
        override val packId = CHICKEN
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_chicken),
            optionDesc = resourceProvider.getString(R.string.gen_chicken_desc),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Chicken)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Chicken)
            ),
            optionPrice = null,
            memesPack = Chicken
        )
    }

    data object Friend : MemesPack() {
        override val packId = DRYZHKO
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_friend),
            optionDesc = resourceProvider.getString(R.string.gen_friend_desc),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Friend)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Friend)
            ),
            optionPrice = null,
            memesPack = Friend
        )
    }

    data object Gachi : MemesPack() {
        override val packId = GACHI
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_gachi),
            optionDesc = resourceProvider.getString(R.string.gen_gachi_desc),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Gachi)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Gachi)
            ),
            optionPrice = null,
            memesPack = Gachi
        )
    }

    data object Intern : MemesPack() {
        override val packId = GACHI
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_intern),
            optionDesc = resourceProvider.getString(R.string.gen_intern_desc),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Intern)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Intern)
            ),
            optionPrice = null,
            memesPack = Intern
        )
    }

    data object Lapen : MemesPack() {
        override val packId = LAPEN
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_lapen),
            optionDesc = String.empty(),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Lapen)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Lapen)
            ),
            optionPrice = null,
            memesPack = Lapen
        )
    }

    data object Mask : MemesPack() {
        override val packId = MASK
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_mask),
            optionDesc = String.empty(),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Mask)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Mask)
            ),
            optionPrice = null,
            memesPack = Mask
        )
    }

    data object Mark : MemesPack() {
        override val packId = MARK
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_mark),
            optionDesc = String.empty(),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Mark)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Mark)
            ),
            optionPrice = null,
            memesPack = Mark
        )
    }

    data object Office : MemesPack() {
        override val packId = OFFICE
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_office),
            optionDesc = String.empty(),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Office)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Office)
            ),
            optionPrice = null,
            memesPack = Office
        )
    }

    data object Other : MemesPack() {
        override val packId = OTHER
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_other),
            optionDesc = String.empty(),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Other)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Other)
            ),
            optionPrice = null,
            memesPack = Other
        )
    }

    data object Rock : MemesPack() {
        override val packId = ROCK
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_rock),
            optionDesc = String.empty(),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Rock)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Rock)
            ),
            optionPrice = null,
            memesPack = Rock
        )
    }

    data object Stat : MemesPack() {
        override val packId = STAT
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_stat),
            optionDesc = resourceProvider.getString(R.string.gen_stat_desc),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Stat)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Stat)
            ),
            optionPrice = null,
            memesPack = Stat
        )
    }

    data object Table : MemesPack() {
        override val packId = TABLE
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_table),
            optionDesc = String.empty(),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Table)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Table)
            ),
            optionPrice = null,
            memesPack = Table
        )
    }

    data object Women : MemesPack() {
        override val packId = WOMAN
        override fun shopCard(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository
        ) = MemesOptionUi(
            optionIcon = R.drawable.ic_rocket,
            optionTitle = resourceProvider.getString(R.string.gen_woman),
            optionDesc = String.empty(),
            freeAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Women)
            ),
            paidAtPackCount = resourceProvider.getQuantityString(
                identifier = R.plurals.gen_memes_count,
                value = memesRepository.getFreeMemesAtPack(Women)
            ),
            optionPrice = null,
            memesPack = Women
        )
    }

    companion object {
        private const val AVENGERS = 4
        private const val BALE = 5
        private const val BOYS = 6
        private const val CHICKEN = 7
        private const val DRYZHKO = 8
        private const val GACHI = 9
        private const val LAPEN = 10
        private const val MASK = 11
        private const val MARK = 12
        private const val OFFICE = 13
        private const val OTHER = 14
        private const val ROCK = 15
        private const val STAT = 16
        private const val TABLE = 17
        private const val WOMAN = 18

        fun getAllPackIdentifiers() = listOf(
            AVENGERS, BALE, BOYS, CHICKEN, DRYZHKO, GACHI, LAPEN, MASK, MARK, OFFICE, OTHER, ROCK, STAT, TABLE, WOMAN
        )

        fun getPackById(id: Int) = when (id) {
            AVENGERS -> Avengers
            BALE -> Bale
            BOYS -> Boys
            CHICKEN -> Chicken
            DRYZHKO -> Friend
            GACHI -> Gachi
            LAPEN -> Lapen
            MASK -> Mask
            MARK -> Mark
            OFFICE -> Office
            OTHER -> Other
            ROCK -> Rock
            STAT -> Stat
            TABLE -> Table
            WOMAN -> Women
            else -> null
        }

        fun getAvailablePacks(ids: List<Int>) = mutableListOf<MemesPack>().apply {
            ids.forEach { id ->
                when (id) {
                    AVENGERS -> add(Avengers)
                    BALE -> add(Bale)
                    BOYS -> add(Boys)
                    CHICKEN -> add(Chicken)
                    DRYZHKO -> add(Friend)
                    GACHI -> add(Gachi)
                    LAPEN -> add(Lapen)
                    MASK -> add(Mask)
                    MARK -> add(Mark)
                    OFFICE -> add(Office)
                    OTHER -> add(Other)
                    ROCK -> add(Rock)
                    STAT -> add(Stat)
                    TABLE -> add(Table)
                    WOMAN -> add(Women)
                }
            }
        }

        fun getPackForShop(
            resourceProvider: ResourceProvider,
            memesRepository: MemesRepository,
            availablePacks: List<MemesPack>
        ): List<MemesOptionUi> {
            return mutableListOf<MemesOptionUi>().apply {
                add(Avengers.shopCard(resourceProvider, memesRepository))
                add(Bale.shopCard(resourceProvider, memesRepository))
                add(Boys.shopCard(resourceProvider, memesRepository))
                add(Chicken.shopCard(resourceProvider, memesRepository))
                add(Friend.shopCard(resourceProvider, memesRepository))
                add(Gachi.shopCard(resourceProvider, memesRepository))
                add(Lapen.shopCard(resourceProvider, memesRepository))
                add(Mask.shopCard(resourceProvider, memesRepository))
                add(Mark.shopCard(resourceProvider, memesRepository))
                add(Office.shopCard(resourceProvider, memesRepository))
                add(Other.shopCard(resourceProvider, memesRepository))
                add(Rock.shopCard(resourceProvider, memesRepository))
                add(Stat.shopCard(resourceProvider, memesRepository))
                add(Table.shopCard(resourceProvider, memesRepository))
                add(Women.shopCard(resourceProvider, memesRepository))
//                add(
//                    StarterPack.shopCard(resourceProvider, memesRepository).copy(
//                        isAlreadyHave = availablePacks.contains(StarterPack)
//                    )
//                )
//                add(
//                    FamousPeople.shopCard(resourceProvider, memesRepository).copy(
//                        isAlreadyHave = availablePacks.contains(FamousPeople)
//                    )
//                )
//                add(
//                    Legends.shopCard(resourceProvider, memesRepository).copy(
//                        isAlreadyHave = availablePacks.contains(Legends)
//                    )
//                )
            }
        }
    }
}