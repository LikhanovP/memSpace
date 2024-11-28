package xm.space.ultimatememspace.business.repositories.reconnectmanager

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.onEach

class ReconnectedManagerRepositoryImpl : ReconnectedManagerRepository {

    private val onlineUsers = MutableStateFlow<List<Int>>(emptyList())

    private var lockUpdate = false

    override suspend fun addOnlineUser(id: Int) {
        if (lockUpdate == false) {
            onlineUsers.value = onlineUsers.value.toMutableList().apply {
                if (!this.contains(id)) add(id)
            }
        }
    }

    override suspend fun changeLockValue(value: Boolean) {
        lockUpdate = value
    }

    override suspend fun startOnlineCheck(callbackTimer: (List<Int>) -> Unit) {
        lockUpdate = false
        val timer = (0..0)
            .asSequence()
            .asFlow()
            .onEach { delay(5000) }

        timer.collect { callbackTimer.invoke(onlineUsers.value) }
    }

    override suspend fun clearUsersList() {
        onlineUsers.value = emptyList()
    }
}