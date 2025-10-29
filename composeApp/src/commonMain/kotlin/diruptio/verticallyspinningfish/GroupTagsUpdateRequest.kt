package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class GroupTagsUpdateRequest(
    val name: String,
    val tags: Set<String>
)
