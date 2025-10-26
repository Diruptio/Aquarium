package diruptio.aquarium.core

import androidx.compose.ui.platform.ClipEntry
import io.ktor.client.engine.HttpClientEngine

interface Platform {
    val name: String
    val httpClientEngine: HttpClientEngine
    val fishManager: FishManager
    fun createClipEntry(text: String): ClipEntry
}

expect fun getPlatform(): Platform
