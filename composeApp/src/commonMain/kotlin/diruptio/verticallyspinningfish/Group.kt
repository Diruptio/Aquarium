package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

/**
 * A group holds configuration for a set of containers
 *
 * @property name The name
 * @property minCount The minimum number of running containers
 * @property minPort The minimum port number for exposure
 * @property deleteOnStop Indicates whether the containers should be deleted when stopped
 * @property tags A set of tags associated with the group
 */
@Serializable
data class Group(val name: String,
                 val minCount: Int,
                 val minPort: Int,
                 val deleteOnStop: Boolean,
                 val tags: Set<String>) {
    val prettyName: String = name
        .split("-")
        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
}
