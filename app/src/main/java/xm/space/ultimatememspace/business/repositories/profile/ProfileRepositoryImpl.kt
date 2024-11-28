package xm.space.ultimatememspace.business.repositories.profile

import android.content.SharedPreferences
import xm.space.ultimatememspace.business.domain.models.connecteduser.ConnectedUser
import xm.space.ultimatememspace.business.domain.models.memes.MemesPack
import xm.space.ultimatememspace.business.repositories.getValue
import xm.space.ultimatememspace.business.repositories.setValue

class ProfileRepositoryImpl(
    preferences: SharedPreferences
) : ProfileRepository {

    private var playerId: Int by preferences

    private var playerName: String by preferences

    private var playerAvatarId: Int by preferences

    private var isProfileEdited: Boolean by preferences

    private var isAdminProfile: Boolean by preferences

    private var isEditModeOn: Boolean = false

    private var availableMemes: String by preferences

    override suspend fun setPlayerData(id: Int, name: String, avatarId: Int) {
        if (!isProfileEdited) {
            availableMemes = MemesPack.getAllPackIdentifiers().joinToString(separator = "#")
        }
        playerId = id
        playerName = name
        playerAvatarId = avatarId
        isProfileEdited = true
    }

    override suspend fun getUserId() = playerId

    override suspend fun getUserAvatarId() = playerAvatarId

    override suspend fun getUserName() = playerName

    override suspend fun isProfileWasEdit() = isProfileEdited

    override fun getFullProfile() = ConnectedUser(
        id = playerId,
        name = playerName,
        avatar = playerAvatarId
    )

    override suspend fun setProfileRole(isAdmin: Boolean) {
        isAdminProfile = isAdmin
    }

    override suspend fun isProfileBelongAdmin() = isAdminProfile

    override suspend fun setStatusEditProfile() {
        isEditModeOn = true
    }

    override fun clearStatusEditProfile() {
        isEditModeOn = false
    }

    override fun getProfileModeStatus() = isEditModeOn

    override suspend fun getAvailableMemesPack(): List<MemesPack> {
        val memesIdentifier = availableMemes.split("#").map { it.toInt() }
        return MemesPack.getAvailablePacks(memesIdentifier)
    }

    override suspend fun addNewMemesPack(memesPack: MemesPack) {
        val memesIdentifier = availableMemes.split("#").toMutableList().apply {
            add(memesPack.packId.toString())
        }
        availableMemes = memesIdentifier.joinToString(separator = "#")
    }
}