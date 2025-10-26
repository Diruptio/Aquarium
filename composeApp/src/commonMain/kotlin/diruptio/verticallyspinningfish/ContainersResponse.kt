package diruptio.verticallyspinningfish

import kotlinx.serialization.Serializable

@Serializable
data class ContainersResponse(val containers: List<Container>)
