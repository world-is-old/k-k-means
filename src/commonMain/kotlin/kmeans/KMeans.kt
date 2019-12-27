package kmeans

import math.Vector
import math.VectorMath
import kotlin.random.Random

open class KMeans(val data: Data, val vectorMath: VectorMath, val random: Random) {

    val minBound: Vector
    val maxBound: Vector

    init {
        val (min, max) = vectorMath.minMaxBound(data.points, data.vectorSize)
        minBound = min
        maxBound = max
    }

    open fun run(clusterNum: Int, maxIterations: Int): AlgorithmResult {
        val timer = Timer()
        var centroids = spawnCenters(clusterNum, data)

        var converged = false
        var iterations = 0


        println("spawn in ${timer.elapsedSec()}")

        while (!converged && iterations < maxIterations) {
            converged = true
            timer.reset()
            val clusters = clusterPoints(centroids, data)
            println("cluster in ${timer.elapsedSec()}")
            val updatedCentroids = mutableListOf<Vector>()
            timer.reset()
            for (centroid in centroids) {
                val points = clusters[centroid]
                val updatedCentroid = if (points == null) randomInBounds() else calculateCenter(points)
                updatedCentroids.add(updatedCentroid)
                converged = converged and (vectorMath.distance(centroid, updatedCentroid) < 1)
            }
            println("center update in ${timer.elapsedSec()}")
            centroids = updatedCentroids
            iterations ++
        }

        return AlgorithmResult(iterations, centroids)
    }

    open fun clusterPoints(centroids: Collection<Vector>, data: Data): Map<Vector, List<Vector>> {
        return data.points.map{ point -> Pair(closestCentroid(vectorMath, point, centroids), point) }
                          .groupBy({ it.component1() }, { it.component2() })
    }

    open fun calculateCenter(points: List<Vector>): Vector {
        return vectorMath.divideBy(points.reduce { v1, v2 -> vectorMath.add(v1, v2) }, points.size)
    }

    open fun spawnCenters(clusterNum: Int, data: Data): List<Vector> {
        return (0 until clusterNum).map {randomInBounds()}.toList()
    }

    fun randomInBounds(): Vector {
        return vectorMath.rand(random, minBound, maxBound)
    }
}

//moving function outside class because it is considered as a captured value when passed inside the context
// (problematic in Native impl)
fun closestCentroid(vectorMath: VectorMath, point: Vector, centroids:Collection<Vector>): Vector {
    return centroids.minBy { vectorMath.distMeasure(it, point) }
        ?: throw IllegalStateException("No centroids")
}

