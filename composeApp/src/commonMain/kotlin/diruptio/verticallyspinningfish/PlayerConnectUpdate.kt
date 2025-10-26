package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class PlayerConnectUpdate(val player: Uuid, val containerId: String) : LiveUpdate {
    override val type: String = "player_connect"
}
