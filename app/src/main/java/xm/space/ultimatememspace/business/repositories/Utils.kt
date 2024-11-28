package xm.space.ultimatememspace.business.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.reflect.KProperty

/**
 * Retrieving a value from the sandbox
 */
inline operator fun <reified T> SharedPreferences.getValue(ref: Any, property: KProperty<*>): T {
    return when (T::class) {
        Boolean::class -> getBoolean(property.name, false)
        String::class -> getString(property.name, String())
        Int::class -> getInt(property.name, Int.MIN_VALUE)
        else -> throw UnsupportedOperationException()
    } as T
}

/**
 * Filling the sandbox value
 */
operator fun <T> SharedPreferences.setValue(ref: Any, property: KProperty<*>, value: T) {
    edit {
        when (value) {
            is Boolean -> putBoolean(property.name, value)
            is String -> putString(property.name, value)
            is Int -> putInt(property.name, value)
            else -> throw UnsupportedOperationException()
        }
    }
}