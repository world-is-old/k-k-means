package kmeans

import kotlin.random.Random

class Data (val points: List<Vector>, vectorSize: Int) {

    val min: Vector
    val max: Vector

    init {
        val zero = Vectors.zero(vectorSize)

        var minV = zero
        var maxV = zero
        var dMin = Double.MAX_VALUE
        var dMax = 0.0

        for (point in points) {
            val dMes = Vectors.distMeasure(zero, point)
            if (dMes < dMin) {
                minV = point
                dMin = dMes
            }
            if (dMes > dMax) {
                maxV = point
                dMax = dMes
            }
        }

        this.min = minV
        max = maxV
    }

    fun randWithin(random: Random): Vector {
        return Vectors.rand(random, min, max)
    }
}