package xm.space.ultimatememspace.business.repositories.profile

import xm.space.ultimatememspace.business.domain.models.connecteduser.ConnectedUser
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack

interface ProfileRepository {

    suspend fun setPlayerData(id: Int, name: String, avatarId: Int)

    suspend fun getUserName(): String

    suspend fun getUserId(): Int

    suspend fun getUserAvatarId(): Int

    suspend fun isProfileWasEdit(): Boolean

    fun getFullProfile(): ConnectedUser

    suspend fun setProfileRole(isAdmin: Boolean)

    suspend fun isProfileBelongAdmin(): Boolean

    /*** Set the profile to edit mode */
    suspend fun setStatusEditProfile()

    /*** Clear profile status */
    fun clearStatusEditProfile()

    /*** Getting profile mode status */
    fun getProfileModeStatus(): Boolean

    suspend fun getAvailableMemesPack(): List<MemesPack>

    suspend fun addNewMemesPack(memesPack: MemesPack)
}