package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Serializable
@OptIn(ExperimentalUuidApi::class)
data class PlayerConnectRequest(val player: Uuid, val containerId: String)
