package xm.space.ultimatememspace.core.providers

/**
 * Mechanism for obtaining string resources
 */
interface ResourceProvider {

    /**
     * Getting a row by id
     * @param identifier Resource id
     */
    fun getString(identifier: Int): String

    /**
     * Getting a row by id and insertion set
     * @param identifier Resource id
     * @param params Insertion set
     */
    fun getString(identifier: Int, vararg params: Any): String

    fun getQuantityString(identifier: Int, value: Int): String
}