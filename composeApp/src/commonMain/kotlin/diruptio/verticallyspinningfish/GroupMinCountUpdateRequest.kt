package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class GroupMinCountUpdateRequest(
    val name: String,
    val minCount: Int
)
