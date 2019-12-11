package kmeans

import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

object Vectors {

    internal fun add(v1: Vector, v2: Vector): Vector {
        assertSizeEq(v1, v2)
        val size = v1.size

        val result = DoubleArray(size)
        for (i in 0 until size) {
            result[i] = v1.values[i] + v2.values[i]
        }
        return Vector(size, result)
    }

    internal fun zero(size: Int): Vector {
        return Vector(size, DoubleArray(size))
    }

    internal fun distance(v1: Vector, v2: Vector): Double {
        return sqrt(distMeasure(v1, v2))
    }

    internal fun distMeasure(v1: Vector, v2: Vector): Double {
        assertSizeEq(v1, v2)

        var sum = 0.0
        for (i in v1.values.indices) {
            val value = v1.values[i]
            val otherValue = v2.values[i]
            sum += (value - otherValue).pow(2.0)
        }
        return sum
    }

    internal fun divideBy(v: Vector, d: Int): Vector {
        val result = DoubleArray(v.size)
        for (i in v.values.indices) {
            result[i] = v.values[i] / d
        }
        return Vector(v.size, result)
    }

    internal fun rand(random: Random, min: Vector, max: Vector): Vector {
        assertSizeEq(min, max)
        val vectorSize = min.size

        val v = DoubleArray(vectorSize)
        for (j in 0 until vectorSize) {
            val nd = random.nextDouble()
            v[j] = nd * (max.values[j] - min.values[j]) + min.values[j]
        }
        return Vector(vectorSize, v)
    }

    private fun assertSizeEq(v1: Vector, v2: Vector) {
        require(v1.size == v2.size) { "Mismatching size" + v1.size + ", " + v2.size }
    }
}
