package math

class Vector internal constructor(internal val values: DoubleArray) {

    val size = values.size

    override fun toString(): String {
        return "Vector(size=$size, values=${values.contentToString()})"
    }
}
