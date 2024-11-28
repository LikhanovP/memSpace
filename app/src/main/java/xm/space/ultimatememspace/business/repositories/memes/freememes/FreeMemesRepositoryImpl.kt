package xm.space.ultimatememspace.business.repositories.memes.freememes

import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.memes.MemeOption
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Boys
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Bale
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Avengers
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Chicken
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Friend
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Gachi
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Intern
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Lapen
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Mark
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Mask
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Office
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Other
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Rock
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Stat
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Table
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack.Women

class FreeMemesRepositoryImpl : FreeMemesRepository {

    override fun getFreeMemesAtPack(pack: MemesPack) = when (pack) {
        is Boys -> getFreeBoys()
        is Bale -> getFreeBale()
        is Avengers -> getFreeAvengers()
        is Chicken -> getFreeChicken()
        is Friend -> getDr()
        is Gachi -> getFreeGachi()
        is Intern -> getFreeIntern()
        is Lapen -> getFreeLap()
        is Mark -> getFreeMark()
        is Mask -> getFreeMask()
        is Office -> getFreeOffice()
        is Other -> getFreeOther()
        is Rock -> getFreeRock()
        is Stat -> getFreeStat()
        is Table -> getFreeTab()
        is Women -> getFreeWomen()
    }

    private fun getFreeAvengers() = listOf(
        MemeOption(memeId = 1005, memeResId = R.drawable.av_up5),
        MemeOption(memeId = 1006, memeResId = R.drawable.av_up6),
        MemeOption(memeId = 1007, memeResId = R.drawable.av_up7),
        MemeOption(memeId = 1008, memeResId = R.drawable.av_up8),
        MemeOption(memeId = 1009, memeResId = R.drawable.av_up9)
    ).shuffled()

    private fun getFreeBale() = listOf(
        MemeOption(memeId = 1102, memeResId = R.drawable.bale2_up),
        MemeOption(memeId = 1104, memeResId = R.drawable.bale4_up),
        MemeOption(memeId = 1107, memeResId = R.drawable.bale7_up),
        MemeOption(memeId = 1108, memeResId = R.drawable.bale8_up),
        MemeOption(memeId = 1110, memeResId = R.drawable.bale10_up)
    ).shuffled()

    private fun getFreeBoys() = listOf(
        MemeOption(memeId = 1200, memeResId = R.drawable.boy_up),
        MemeOption(memeId = 1201, memeResId = R.drawable.boy1_up),
        MemeOption(memeId = 1206, memeResId = R.drawable.boys6_up),
        MemeOption(memeId = 1207, memeResId = R.drawable.boys7_up),
        MemeOption(memeId = 1209, memeResId = R.drawable.boys9_up)
    ).shuffled()

    private fun getFreeChicken() = listOf(
        MemeOption(memeId = 1300, memeResId = R.drawable.chi_up),
        MemeOption(memeId = 1301, memeResId = R.drawable.chi1_up),
        MemeOption(memeId = 1302, memeResId = R.drawable.chi2_up),
        MemeOption(memeId = 1303, memeResId = R.drawable.chi3_up),
        MemeOption(memeId = 1304, memeResId = R.drawable.chi4_up),
        MemeOption(memeId = 1305, memeResId = R.drawable.chi5_up),
        MemeOption(memeId = 1306, memeResId = R.drawable.chi6_up),
        MemeOption(memeId = 1307, memeResId = R.drawable.chi7_up),
        MemeOption(memeId = 1308, memeResId = R.drawable.chi8_up),
        MemeOption(memeId = 1309, memeResId = R.drawable.chi9_up)
    ).shuffled()

    private fun getFreeGachi() = listOf(
        MemeOption(memeId = 1500, memeResId = R.drawable.ga_up),
        MemeOption(memeId = 1501, memeResId = R.drawable.ga1_up),
        MemeOption(memeId = 1504, memeResId = R.drawable.ga4_up),
        MemeOption(memeId = 1507, memeResId = R.drawable.ga7_up),
        MemeOption(memeId = 1509, memeResId = R.drawable.ga9_up)
    ).shuffled()

    private fun getFreeIntern() = listOf(
        MemeOption(memeId = 1603, memeResId = R.drawable.in3_up),
        MemeOption(memeId = 1604, memeResId = R.drawable.in4_up),
        MemeOption(memeId = 1606, memeResId = R.drawable.in6_up),
        MemeOption(memeId = 1607, memeResId = R.drawable.in7_up),
        MemeOption(memeId = 1608, memeResId = R.drawable.in8_up),
    ).shuffled()

    private fun getFreeLap() = listOf(
        MemeOption(memeId = 1700, memeResId = R.drawable.la_up),
        MemeOption(memeId = 1701, memeResId = R.drawable.la1_up),
        MemeOption(memeId = 1702, memeResId = R.drawable.la2_up),
        MemeOption(memeId = 1703, memeResId = R.drawable.la3_up),
        MemeOption(memeId = 1704, memeResId = R.drawable.la4_up)
    ).shuffled()

    private fun getFreeMask() = listOf(
        MemeOption(memeId = 1900, memeResId = R.drawable.ma_up),
        MemeOption(memeId = 1901, memeResId = R.drawable.ma1_up),
        MemeOption(memeId = 1902, memeResId = R.drawable.ma2_up),
        MemeOption(memeId = 1903, memeResId = R.drawable.ma3_up),
        MemeOption(memeId = 1904, memeResId = R.drawable.ma4_up),
        MemeOption(memeId = 1905, memeResId = R.drawable.ma5_up),
        MemeOption(memeId = 1906, memeResId = R.drawable.ma6_up),
        MemeOption(memeId = 1907, memeResId = R.drawable.ma7_up),
        MemeOption(memeId = 1908, memeResId = R.drawable.ma8_up),
        MemeOption(memeId = 1909, memeResId = R.drawable.ma9_up)
    ).shuffled()

    private fun getFreeMark() = listOf(
        MemeOption(memeId = 2000, memeResId = R.drawable.mark_up),
        MemeOption(memeId = 2002, memeResId = R.drawable.mark2_up),
        MemeOption(memeId = 2003, memeResId = R.drawable.mark3_up),
        MemeOption(memeId = 2007, memeResId = R.drawable.mark7_up),
        MemeOption(memeId = 2008, memeResId = R.drawable.mark8_up),
    ).shuffled()

    private fun getFreeOther() = listOf(
        MemeOption(memeId = 2204, memeResId = R.drawable.other4_up),
        MemeOption(memeId = 2205, memeResId = R.drawable.other5_up),
        MemeOption(memeId = 2206, memeResId = R.drawable.other6_up),
        MemeOption(memeId = 2207, memeResId = R.drawable.other7_up),
        MemeOption(memeId = 2211, memeResId = R.drawable.other11_up),
        MemeOption(memeId = 2214, memeResId = R.drawable.other14_up),
        MemeOption(memeId = 2216, memeResId = R.drawable.other16_up),
        MemeOption(memeId = 2218, memeResId = R.drawable.other18_up),
        MemeOption(memeId = 2219, memeResId = R.drawable.other19_up),
        MemeOption(memeId = 1800, memeResId = R.drawable.other20_up)
    ).shuffled()

    private fun getFreeRock() = listOf(
        MemeOption(memeId = 2302, memeResId = R.drawable.rock2_up),
        MemeOption(memeId = 2303, memeResId = R.drawable.rock3_up),
        MemeOption(memeId = 2307, memeResId = R.drawable.rock7_up),
        MemeOption(memeId = 2308, memeResId = R.drawable.rock8_up),
        MemeOption(memeId = 2309, memeResId = R.drawable.rock9_up)
    ).shuffled()

    private fun getFreeStat() = listOf(
        MemeOption(memeId = 2400, memeResId = R.drawable.stat_up),
        MemeOption(memeId = 2401, memeResId = R.drawable.stat1_up),
        MemeOption(memeId = 2402, memeResId = R.drawable.stat2_up),
        MemeOption(memeId = 2407, memeResId = R.drawable.stat7_up),
        MemeOption(memeId = 2409, memeResId = R.drawable.stat9_up)
    ).shuffled()

    private fun getFreeTab() = listOf(
        MemeOption(memeId = 2500, memeResId = R.drawable.tab_up),
        MemeOption(memeId = 2502, memeResId = R.drawable.tab2_up),
        MemeOption(memeId = 2503, memeResId = R.drawable.tab3_up),
        MemeOption(memeId = 2504, memeResId = R.drawable.tab4_up),
        MemeOption(memeId = 2508, memeResId = R.drawable.tab8_up),
        MemeOption(memeId = 2509, memeResId = R.drawable.tab9_up),
        MemeOption(memeId = 2510, memeResId = R.drawable.tab10_up)
    ).shuffled()

    private fun getFreeWomen() = listOf(
        MemeOption(memeId = 2602, memeResId = R.drawable.wom2),
        MemeOption(memeId = 2605, memeResId = R.drawable.wom5),
        MemeOption(memeId = 2606, memeResId = R.drawable.wom6),
        MemeOption(memeId = 2607, memeResId = R.drawable.wom7),
        MemeOption(memeId = 2608, memeResId = R.drawable.wom8),
        MemeOption(memeId = 2611, memeResId = R.drawable.wom11)
    ).shuffled()

    private fun getFreeOffice() = listOf(
        MemeOption(memeId = 2100, memeResId = R.drawable.off_up),
        MemeOption(memeId = 2101, memeResId = R.drawable.off_up1),
        MemeOption(memeId = 2102, memeResId = R.drawable.off_up2),
        MemeOption(memeId = 2103, memeResId = R.drawable.off_up3),
        MemeOption(memeId = 2104, memeResId = R.drawable.off_up4),
        MemeOption(memeId = 2105, memeResId = R.drawable.off_up5),
        MemeOption(memeId = 2106, memeResId = R.drawable.off_up6),
        MemeOption(memeId = 2107, memeResId = R.drawable.off_up7),
        MemeOption(memeId = 2108, memeResId = R.drawable.off_up8),
        MemeOption(memeId = 2109, memeResId = R.drawable.off_up9)
    ).shuffled()

    private fun getDr() = listOf(
        MemeOption(memeId = 1400, memeResId = R.drawable.dr_up),
        MemeOption(memeId = 1401, memeResId = R.drawable.dr1_up),
        MemeOption(memeId = 1402, memeResId = R.drawable.dr2_up)
    ).shuffled()
}