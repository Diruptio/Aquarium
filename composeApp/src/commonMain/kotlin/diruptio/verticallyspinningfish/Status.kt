package diruptio.verticallyspinningfish

/**
 * Represents the status of a container
 *
 * @property isOnline Indicates if the status is considered online
 */
enum class Status(val isOnline: Boolean) {
    OFFLINE(false),
    ONLINE(true),
    AVAILABLE(true),
    UNAVAILABLE(true);

    /** Indicates if the status is considered offline */
    val isOffline = !isOnline
}
