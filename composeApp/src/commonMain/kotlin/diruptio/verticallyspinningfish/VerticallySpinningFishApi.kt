@file:OptIn(ExperimentalUuidApi::class)

package diruptio.verticallyspinningfish

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.websocket.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class VerticallySpinningFishApi(clientEngine: HttpClientEngine,
                                val baseUrl: String,
                                val secret: String) {
    private val httpClient = HttpClient(clientEngine) {
        install(ContentNegotiation) {
            json()
        }
        defaultRequest {
            header("Authorization", secret)
        }
    }
    private val wsClient = HttpClient(clientEngine) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
        defaultRequest {
            header("Authorization", secret)
        }
    }
    var closed = false
        private set

    var containerPrefix: String = ""
    var groups: List<Group> = emptyList()
        private set
    var containers: List<Container> = emptyList()
        private set

    val containerAddListeners: MutableList<(Container) -> Unit> = mutableListOf()
    val containerRemoveListeners: MutableList<(Container) -> Unit> = mutableListOf()
    val containerStatusListeners: MutableList<(Container) -> Unit> = mutableListOf()
    val playerConnectListeners: MutableList<(PlayerConnectUpdate) -> Unit> = mutableListOf()
    val groupUpdateListeners: MutableList<(Group) -> Unit> = mutableListOf()

    suspend fun open(coroutineScope: CoroutineScope) {
        val deferred = CompletableDeferred<Unit>()
        coroutineScope.launch {
            while (!closed) {
                if (connect()) {
                    if (!deferred.isCompleted) {
                        deferred.complete(Unit)
                    }
                    liveUpdates()
                }
                if (!closed) {
                    println("Connection to VerticallySpinningFish API failed or was lost. Retrying in 15 seconds...")
                    delay(15000)
                }
            }
        }
        deferred.join()
    }

    fun close() {
        closed = true
        httpClient.close()
        wsClient.close()
    }

    private suspend fun connect(): Boolean {
        try {
            containerPrefix = Json.decodeFromString<PrefixResponse>(httpClient.get("$baseUrl/prefix").bodyAsText()).prefix
            groups = Json.decodeFromString<GroupsResponse>(httpClient.get("$baseUrl/groups").bodyAsText()).groups
            containers = Json.decodeFromString<ContainersResponse>(httpClient.get("$baseUrl/containers").bodyAsText()).containers
            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    private suspend fun liveUpdates() {
        try {
            wsClient.webSocket("${baseUrl.replace("http", "ws")}/live-updates") {
                println("Successfully connected to VerticallySpinningFish API live updates.")
                while (true) {
                    val updateType = (incoming.receive() as Frame.Text).readText()
                    val data = (incoming.receive() as Frame.Text).readText()
                    handleLiveUpdate(updateType, data)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleLiveUpdate(updateType: String, data: String) {
        when (updateType) {
            "container_add" -> {
                val update = Json.decodeFromString<ContainerAddUpdate>(data)
                containers = containers + update.container
                containerAddListeners.forEach { listener ->
                    try { listener(update.container) } catch (_: Throwable) {}
                }
            }
            "container_remove" -> {
                val update = Json.decodeFromString<ContainerRemoveUpdate>(data)
                val container = containers.find { it.id == update.id }
                if (container != null) {
                    containers = containers - container
                    containerRemoveListeners.forEach { listener ->
                        try { listener(container) } catch (_: Throwable) {}
                    }
                }
            }
            "container_status" -> {
                val update = Json.decodeFromString<ContainerStatusUpdate>(data)
                containers.find { it.id == update.id }?.let { container ->
                    container.setStatus(update.status)
                    containerStatusListeners.forEach { listener ->
                        try { listener(container) } catch (_: Throwable) {}
                    }
                }
            }
            "player_connect" -> {
                val update = Json.decodeFromString<PlayerConnectUpdate>(data)
                playerConnectListeners.forEach { listener ->
                    try { listener(update) } catch (_: Throwable) {}
                }
            }
            "group_min_count" -> {
                val update = Json.decodeFromString<GroupMinCountUpdate>(data)
                updateGroupProperty(update.name) { it.copy(minCount = update.minCount) }
            }
            "group_min_port" -> {
                val update = Json.decodeFromString<GroupMinPortUpdate>(data)
                updateGroupProperty(update.name) { it.copy(minPort = update.minPort) }
            }
            "group_delete_on_stop" -> {
                val update = Json.decodeFromString<GroupDeleteOnStopUpdate>(data)
                updateGroupProperty(update.name) { it.copy(deleteOnStop = update.deleteOnStop) }
            }
            "group_tags" -> {
                val update = Json.decodeFromString<GroupTagsUpdate>(data)
                updateGroupProperty(update.name) { it.copy(tags = update.tags) }
            }
        }
    }

    private fun updateGroupProperty(groupName: String, updateFn: (Group) -> Group) {
        val group = groups.find { it.name == groupName }
        if (group != null) {
            val updatedGroup = updateFn(group)
            groups = groups - group + updatedGroup
            groupUpdateListeners.forEach { listener ->
                try { listener(updatedGroup) } catch (_: Throwable) {}
            }
        }
    }

    fun getGroupByName(name: String): Group? = groups.find { it.name == name }

    fun getGroupByContainer(containerName: String): Group? {
        return groups.find { containerName.startsWith("$containerPrefix${it.name}-") }
    }

    fun getContainersByGroup(name: String): List<Container> {
        val prefix = "$containerPrefix$name-"
        return containers.filter { it.name.startsWith(prefix) }
    }

    fun getContainer(id: String): Container? =
        containers.find { it.id.startsWith(id) || id.startsWith(it.id) }

    suspend fun createContainer(group: String): Container? {
        try {
            return httpClient.post("$baseUrl/container") {
                contentType(ContentType.Application.Json)
                setBody(ContainerCreateRequest(group))
            }.body<ContainerCreateResponse>().container
        } catch (e: Exception) {
            println("Failed to create container for group $group: ${e.message}")
            return null
        }
    }

    suspend fun startContainer(containerId: String) {
        try {
            httpClient.post("$baseUrl/container/start") {
                contentType(ContentType.Application.Json)
                setBody(ContainerStartRequest(containerId))
            }
        } catch (e: Exception) {
            println("Failed to start container $containerId: ${e.message}")
        }
    }

    suspend fun stopContainer(containerId: String) {
        try {
            httpClient.post("$baseUrl/container/stop") {
                contentType(ContentType.Application.Json)
                setBody(ContainerStopRequest(containerId))
            }
        } catch (e: Exception) {
            println("Failed to stop container $containerId: ${e.message}")
        }
    }

    suspend fun connectPlayer(player: Uuid, containerId: String) {
        try {
            httpClient.post("$baseUrl/player/connect") {
                contentType(ContentType.Application.Json)
                setBody(PlayerConnectRequest(player, containerId))
            }
        } catch (e: Exception) {
            println("Failed to connect player $player to container $containerId: ${e.message}")
        }
    }

    suspend fun setContainerStatus(containerId: String, status: Status) {
        try {
            httpClient.patch("$baseUrl/container/status") {
                contentType(ContentType.Application.Json)
                setBody(ContainerStatusRequest(containerId, status))
            }
        } catch (e: Exception) {
            println("Failed to set status of container $containerId to $status: ${e.message}")
        }
    }

    suspend fun updateGroupMinCount(name: String, minCount: Int) {
        try {
            httpClient.patch("$baseUrl/group/min-count") {
                contentType(ContentType.Application.Json)
                setBody(GroupMinCountUpdateRequest(name, minCount))
            }
        } catch (e: Exception) {
            println("Failed to update min-count of group $name: ${e.message}")
        }
    }

    suspend fun updateGroupMinPort(name: String, minPort: Int) {
        try {
            httpClient.patch("$baseUrl/group/min-port") {
                contentType(ContentType.Application.Json)
                setBody(GroupMinPortUpdateRequest(name, minPort))
            }
        } catch (e: Exception) {
            println("Failed to update min-port of group $name: ${e.message}")
        }
    }

    suspend fun updateGroupDeleteOnStop(name: String, deleteOnStop: Boolean) {
        try {
            httpClient.patch("$baseUrl/group/delete-on-stop") {
                contentType(ContentType.Application.Json)
                setBody(GroupDeleteOnStopUpdateRequest(name, deleteOnStop))
            }
        } catch (e: Exception) {
            println("Failed to update delete-on-stop of group $name: ${e.message}")
        }
    }

    suspend fun updateGroupTags(name: String, tags: Set<String>) {
        try {
            httpClient.patch("$baseUrl/group/tags") {
                contentType(ContentType.Application.Json)
                setBody(GroupTagsUpdateRequest(name, tags))
            }
        } catch (e: Exception) {
            println("Failed to update tags of group $name: ${e.message}")
        }
    }
}
