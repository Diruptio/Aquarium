package diruptio.aquarium.core

import androidx.compose.ui.platform.ClipEntry
import io.ktor.client.engine.js.Js
import kotlinx.browser.document
import kotlinx.serialization.json.Json

external fun encodeURIComponent(str: String): String
external fun decodeURIComponent(str: String): String

fun getCookies(): Map<String, String> {
    val cookies = mutableMapOf<String, String>()
    for (cookie in document.cookie.split(";")) {
        val decodedCookie = cookie.trim().split("=").map { decodeURIComponent(it) }
        if (decodedCookie.size == 2) {
            cookies[decodedCookie[0]] = decodedCookie[1]
        }
    }
    return cookies.toMap()
}

fun getCookie(name: String): String? = getCookies()[name]

fun setCookie(name: String, value: String) {
    val cookies = getCookies().toMutableMap()
    cookies[name] = value
    document.cookie = cookies
        .map { entry -> "${encodeURIComponent(entry.key)}=${encodeURIComponent(entry.value)}" }
        .joinToString("; ")
}

class WasmFishManager : FishManager {
    override var fishes: List<Fish> = Json.decodeFromString(getCookie("fishes")?: "[]")
        set(value) {
            setCookie("fishes", Json.encodeToString(value))
            field = value
        }
}

class WasmPlatform : Platform {
    override val name: String = "Web"
    override val httpClientEngine = Js.create()
    override val fishManager: FishManager = WasmFishManager()
    override fun createClipEntry(text: String): ClipEntry = ClipEntry.withPlainText(text)
}

actual fun getPlatform(): Platform = WasmPlatform()
