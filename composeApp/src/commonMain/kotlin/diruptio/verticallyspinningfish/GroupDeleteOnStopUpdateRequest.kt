package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class GroupDeleteOnStopUpdateRequest(
    val name: String,
    val deleteOnStop: Boolean
)
