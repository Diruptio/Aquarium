package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class ContainerStatusRequest(val id: String, val status: Status)
