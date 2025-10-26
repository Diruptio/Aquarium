package diruptio.aquarium.core

import kotlinx.serialization.Serializable

@Serializable
data class Fish(val name: String, val url: String, val secret: String)
