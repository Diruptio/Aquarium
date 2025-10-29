package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class GroupMinPortUpdate(
    val name: String,
    val minPort: Int
)
