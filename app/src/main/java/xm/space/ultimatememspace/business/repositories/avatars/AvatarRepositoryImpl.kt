package xm.space.ultimatememspace.business.repositories.avatars

import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.business.domain.models.avatar.Avatar

class AvatarRepositoryImpl : AvatarRepository {

    override suspend fun getAvatars(): List<Avatar> = listOf(
        Avatar(
            iconId = 0,
            iconResId = R.drawable.ic_avatar_one
        ),
        Avatar(
            iconId = 1,
            iconResId = R.drawable.ic_avatar_two
        ),
        Avatar(
            iconId = 2,
            iconResId = R.drawable.ic_avatar_three
        ),
        Avatar(
            iconId = 3,
            iconResId = R.drawable.ic_avatar_four
        ),
        Avatar(
            iconId = 4,
            iconResId = R.drawable.ic_avatar_five
        ),
        Avatar(
            iconId = 5,
            iconResId = R.drawable.ic_avatar_six
        ),
        Avatar(
            iconId = 6,
            iconResId = R.drawable.ic_avatar_seven
        ),
        Avatar(
            iconId = 7,
            iconResId = R.drawable.ic_avatar_eight
        ),
        Avatar(
            iconId = 8,
            iconResId = R.drawable.ic_avatar_nine
        ),
        Avatar(
            iconId = 9,
            iconResId = R.drawable.ic_avatar_ten
        ),
        Avatar(
            iconId = 10,
            iconResId = R.drawable.ic_avatar_eleven
        ),
        Avatar(
            iconId = 11,
            iconResId = R.drawable.ic_avatar_twelve
        )
    )
}