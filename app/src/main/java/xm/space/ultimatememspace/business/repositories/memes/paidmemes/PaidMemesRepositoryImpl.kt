package xm.space.ultimatememspace.business.repositories.memes.paidmemes

import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.memes.MemeOption
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Boys
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Bale
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Avengers
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Gachi
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Intern
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Mark
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Other
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Rock
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Stat
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Table
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Women

class PaidMemesRepositoryImpl : PaidMemesRepository {

    override fun getPaidMemesAtPack(pack: MemesPack) = when (pack) {
        is Boys -> getPaidBoys()
        is Bale -> getPaidBale()
        is Avengers -> getPaidAvengers()
        is Gachi -> getPaidGachi()
        is Intern -> getPaidIntern()
        is Mark -> getPaidMark()
        is Other -> getPaidOther()
        is Rock -> getPaidRock()
        is Stat -> getPaidStat()
        is Table -> getPaidTab()
        is Women -> getPaidWomen()
        else -> emptyList()
    }

    private fun getPaidAvengers() = listOf(
        MemeOption(memeId = 1000, memeResId = R.drawable.av_up, isFree = false),
        MemeOption(memeId = 1001, memeResId = R.drawable.av_up1, isFree = false),
        MemeOption(memeId = 1002, memeResId = R.drawable.av_up2, isFree = false),
        MemeOption(memeId = 1003, memeResId = R.drawable.av_up3, isFree = false),
        MemeOption(memeId = 1004, memeResId = R.drawable.av_up4, isFree = false)
    ).shuffled()

    private fun getPaidBale() = listOf(
        MemeOption(memeId = 1100, memeResId = R.drawable.bale_up, isFree = false),
        MemeOption(memeId = 1101, memeResId = R.drawable.bale1_up, isFree = false),
        MemeOption(memeId = 1103, memeResId = R.drawable.bale3_up, isFree = false),
        MemeOption(memeId = 1105, memeResId = R.drawable.bale5_up, isFree = false),
        MemeOption(memeId = 1106, memeResId = R.drawable.bale6_up, isFree = false),
        MemeOption(memeId = 1109, memeResId = R.drawable.bale9_up, isFree = false)
    ).shuffled()

    private fun getPaidBoys() = listOf(
        MemeOption(memeId = 1202, memeResId = R.drawable.boy2_up, isFree = false),
        MemeOption(memeId = 1203, memeResId = R.drawable.boy3_up, isFree = false),
        MemeOption(memeId = 1204, memeResId = R.drawable.boys4_up, isFree = false),
        MemeOption(memeId = 1205, memeResId = R.drawable.boys5_up, isFree = false),
        MemeOption(memeId = 1208, memeResId = R.drawable.boys8_up, isFree = false)
    ).shuffled()

    private fun getPaidGachi() = listOf(
        MemeOption(memeId = 1502, memeResId = R.drawable.ga2_up, isFree = false),
        MemeOption(memeId = 1503, memeResId = R.drawable.ga3_up, isFree = false),
        MemeOption(memeId = 1505, memeResId = R.drawable.ga5_up, isFree = false),
        MemeOption(memeId = 1506, memeResId = R.drawable.ga6_up, isFree = false),
        MemeOption(memeId = 1508, memeResId = R.drawable.ga8_up, isFree = false)
    ).shuffled()

    private fun getPaidIntern() = listOf(
        MemeOption(memeId = 1600, memeResId = R.drawable.in_up, isFree = false),
        MemeOption(memeId = 1601, memeResId = R.drawable.in1_up, isFree = false),
        MemeOption(memeId = 1602, memeResId = R.drawable.in2_up, isFree = false),
        MemeOption(memeId = 1605, memeResId = R.drawable.in5_up, isFree = false),
        MemeOption(memeId = 1609, memeResId = R.drawable.in9_up, isFree = false)
    ).shuffled()

    private fun getPaidMark() = listOf(
        MemeOption(memeId = 2001, memeResId = R.drawable.mark1_up, isFree = false),
        MemeOption(memeId = 2004, memeResId = R.drawable.mark4_up, isFree = false),
        MemeOption(memeId = 2005, memeResId = R.drawable.mark5_up, isFree = false),
        MemeOption(memeId = 2006, memeResId = R.drawable.mark6_up, isFree = false),
        MemeOption(memeId = 2009, memeResId = R.drawable.mark9_up, isFree = false)
    ).shuffled()

    private fun getPaidOther() = listOf(
        MemeOption(memeId = 2200, memeResId = R.drawable.other_up, isFree = false),
        MemeOption(memeId = 2202, memeResId = R.drawable.other2_up, isFree = false),
        MemeOption(memeId = 2203, memeResId = R.drawable.other3_up, isFree = false),
        MemeOption(memeId = 2208, memeResId = R.drawable.other8_up, isFree = false),
        MemeOption(memeId = 2209, memeResId = R.drawable.other9_up, isFree = false),
        MemeOption(memeId = 2210, memeResId = R.drawable.other10_up, isFree = false),
        MemeOption(memeId = 2212, memeResId = R.drawable.other12_up, isFree = false),
        MemeOption(memeId = 2213, memeResId = R.drawable.other13_up, isFree = false),
        MemeOption(memeId = 2215, memeResId = R.drawable.other15_up, isFree = false),
        MemeOption(memeId = 2217, memeResId = R.drawable.other17_up, isFree = false),
        MemeOption(memeId = 2211, memeResId = R.drawable.other11_up, isFree = false),
        MemeOption(memeId = 2201, memeResId = R.drawable.other1_up, isFree = false)
    ).shuffled()

    private fun getPaidRock() = listOf(
        MemeOption(memeId = 2300, memeResId = R.drawable.rock_up, isFree = false),
        MemeOption(memeId = 2301, memeResId = R.drawable.rock1_up, isFree = false),
        MemeOption(memeId = 2304, memeResId = R.drawable.rock4_up, isFree = false),
        MemeOption(memeId = 2305, memeResId = R.drawable.rock5_up, isFree = false),
        MemeOption(memeId = 2306, memeResId = R.drawable.rock6_up, isFree = false)
    ).shuffled()

    private fun getPaidStat() = listOf(
        MemeOption(memeId = 2403, memeResId = R.drawable.stat3_up, isFree = false),
        MemeOption(memeId = 2404, memeResId = R.drawable.stat4_up, isFree = false),
        MemeOption(memeId = 2405, memeResId = R.drawable.stat5_up, isFree = false),
        MemeOption(memeId = 2406, memeResId = R.drawable.stat6_up, isFree = false),
        MemeOption(memeId = 2408, memeResId = R.drawable.stat8_up, isFree = false)
    ).shuffled()

    private fun getPaidTab() = listOf(
        MemeOption(memeId = 2501, memeResId = R.drawable.tab1_up, isFree = false),
        MemeOption(memeId = 2505, memeResId = R.drawable.tab5_up, isFree = false),
        MemeOption(memeId = 2506, memeResId = R.drawable.tab6_up, isFree = false),
        MemeOption(memeId = 2507, memeResId = R.drawable.tab7_up, isFree = false)
    ).shuffled()

    private fun getPaidWomen() = listOf(
        MemeOption(memeId = 2600, memeResId = R.drawable.wom, isFree = false),
        MemeOption(memeId = 2601, memeResId = R.drawable.wom1, isFree = false),
        MemeOption(memeId = 2604, memeResId = R.drawable.wom4, isFree = false),
        MemeOption(memeId = 2609, memeResId = R.drawable.wom9, isFree = false),
        MemeOption(memeId = 2610, memeResId = R.drawable.wom10, isFree = false),
        MemeOption(memeId = 2612, memeResId = R.drawable.wom12, isFree = false)
    )
}