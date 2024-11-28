package xm.space.ultimatememspace.business.repositories.avatars

import xm.space.ultimatememspace.business.domain.models.avatar.Avatar

interface AvatarRepository {

    suspend fun getAvatars(): List<Avatar>
}