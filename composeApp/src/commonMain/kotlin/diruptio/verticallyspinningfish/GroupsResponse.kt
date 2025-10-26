package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class GroupsResponse(val groups: List<Group>)
