package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class GroupMinCountUpdate(
    val name: String,
    val minCount: Int
)
