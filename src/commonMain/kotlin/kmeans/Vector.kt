package kmeans

class Vector internal constructor(internal val size: Int, internal val values: DoubleArray) {

    init {
        require(values.size == size) { "Bad size: expected " + size + " vs " + values.size }
    }

    override fun toString(): String {
        return "Vector(size=$size, values=${values.contentToString()})"
    }
}
