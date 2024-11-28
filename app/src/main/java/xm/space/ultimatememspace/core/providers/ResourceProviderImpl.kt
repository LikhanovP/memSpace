package xm.space.ultimatememspace.core.providers

import android.content.Context
import xm.space.ultimatememspace.core.extensions.empty

/**
 * Implementation [ResourceProvider]
 * @property context App context
 */
class ResourceProviderImpl(private val context: Context) : ResourceProvider {

    override fun getString(identifier: Int) = context.getString(identifier)

    override fun getString(identifier: Int, vararg params: Any): String {
        return try { context.getString(identifier, params) } catch (e: Exception) { String.empty() }
    }

    override fun getQuantityString(identifier: Int, value: Int): String {
        return try { context.resources.getQuantityString(identifier, value, value) } catch (e: Exception) { String.empty() }
    }
}