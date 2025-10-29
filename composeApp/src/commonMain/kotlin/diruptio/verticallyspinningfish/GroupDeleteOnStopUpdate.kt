package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class GroupDeleteOnStopUpdate(
    val name: String,
    val deleteOnStop: Boolean
)
