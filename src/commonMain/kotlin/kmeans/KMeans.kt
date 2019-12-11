package kmeans

import kotlin.random.Random

open class KMeans(val data: Data, val random: Random) {

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
                val updatedCentroid = if (points == null) data.randWithin(random) else calculateCenter(points)
                updatedCentroids.add(updatedCentroid)
                converged = converged and (Vectors.distance(centroid, updatedCentroid) < 1)
            }
            println("center update in ${timer.elapsedSec()}")
            centroids = updatedCentroids
            iterations ++
        }

        return AlgorithmResult(iterations, centroids)
    }

    open fun clusterPoints(centroids: Collection<Vector>, data: Data): Map<Vector, List<Vector>> {
        return data.points.map{ point -> Pair(closestCentroid(point, centroids), point) }.groupBy({ it.component1() }, { it.component2() })
    }

    open fun calculateCenter(points: List<Vector>): Vector {
        return Vectors.divideBy(points.reduce { v1, v2 -> Vectors.add(v1, v2) }, points.size)
    }

    open fun spawnCenters(clusterNum: Int, data: Data): List<Vector> {
        return (0 until clusterNum).map {data.randWithin(random)}.toList()
    }
}

fun closestCentroid(point: Vector, centroids:Collection<Vector>): Vector {
    return centroids.minBy { Vectors.distMeasure(it, point) }
        ?: throw IllegalStateException("No centroids")
}

