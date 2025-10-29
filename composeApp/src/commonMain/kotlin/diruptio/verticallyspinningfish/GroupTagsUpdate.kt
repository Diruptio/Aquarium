package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class GroupTagsUpdate(
    val name: String,
    val tags: Set<String>
)
