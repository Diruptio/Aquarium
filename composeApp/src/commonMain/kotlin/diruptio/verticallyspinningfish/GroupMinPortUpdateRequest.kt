package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class GroupMinPortUpdateRequest(
    val name: String,
    val minPort: Int
)
