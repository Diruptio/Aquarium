package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class ContainerStatusUpdate(val id: String, val status: Status) : LiveUpdate {
    override val type: String = "container_status"
}
