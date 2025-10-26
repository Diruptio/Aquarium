package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class ContainerRemoveUpdate(val id: String) : LiveUpdate {
    override val type: String = "container_remove"
}
