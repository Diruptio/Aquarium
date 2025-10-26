package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class ContainerAddUpdate(val container: Container) : LiveUpdate {
    override val type: String = "container_add"
}
