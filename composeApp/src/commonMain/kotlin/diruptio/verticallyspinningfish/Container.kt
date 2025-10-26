package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

/**
 * Represents a Docker container
 *
 * @property id The unique identifier for the container
 * @property name The name of the container
 * @property ports The list of ports exposed by the container
 * @property status The current status of the container
 */
@Serializable
data class Container(val id: String,
                     val name: String,
                     val ports: List<Int>,
                     private var status: Status) {
    fun getStatus(): Status = status

    internal fun setStatus(status: Status) {
        this.status = status
    }

    fun getPrettyName(api: VerticallySpinningFishApi): String {
        return name
            .substringAfter(api.containerPrefix)
            .substringBeforeLast("-")
            .split("-")
            .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
            .plus(" #")
            .plus(name.substringAfterLast("-"))
    }
}
