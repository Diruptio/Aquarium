package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class GroupUpdateRequest(
    val name: String,
    val minCount: Int? = null,
    val minPort: Int? = null,
    val deleteOnStop: Boolean? = null,
    val tags: Set<String>? = null
)
