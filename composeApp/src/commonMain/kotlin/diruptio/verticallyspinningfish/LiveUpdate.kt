package diruptio.verticallyspinningfish

/** Represents a live update event */
interface LiveUpdate {
    /** The type of live update must be unique */
    val type: String
}
