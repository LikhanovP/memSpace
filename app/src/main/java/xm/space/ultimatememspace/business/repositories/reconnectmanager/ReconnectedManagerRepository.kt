package xm.space.ultimatememspace.business.repositories.reconnectmanager

interface ReconnectedManagerRepository {

    suspend fun addOnlineUser(id: Int)

    suspend fun changeLockValue(value: Boolean)

    suspend fun startOnlineCheck(callbackTimer: (List<Int>) -> Unit)

    suspend fun clearUsersList()
}