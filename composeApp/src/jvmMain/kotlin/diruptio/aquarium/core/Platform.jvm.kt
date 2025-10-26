package diruptio.aquarium.core

import androidx.compose.ui.platform.ClipEntry
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.serialization.json.Json
import java.awt.datatransfer.StringSelection
import java.nio.file.Files
import kotlin.io.path.Path

class JVMFishManager : FishManager {
    private val directory = Path(System.getProperty("user.home"), ".aquarium")
    private val file = directory.resolve("fishes.json")

    override var fishes: List<Fish> = if (Files.exists(file)) {
        Json.decodeFromString(Files.readString(file))
    } else {
        emptyList()
    }
        set(value) {
            Files.createDirectories(directory)
            Files.writeString(file, Json.encodeToString(value))
            field = value
        }
}

class JVMPlatform : Platform {
    override val name: String = "Desktop"
    override val httpClientEngine: HttpClientEngine = OkHttp.create()
    override val fishManager: FishManager = JVMFishManager()
    override fun createClipEntry(text: String): ClipEntry = ClipEntry(StringSelection(text))
}

actual fun getPlatform(): Platform = JVMPlatform()
