package kmeans

import math.Vector
import math.VectorMath
import kotlin.native.concurrent.*
import kotlin.random.Random

actual class KMeansParallel actual constructor(data: Data, vectorMath: VectorMath, random: Random, val threadNum: Int):
    KMeans(data, vectorMath, random) {

    private lateinit var workers: Array<Worker>

    override fun run(clusterNum: Int, maxIterations: Int): AlgorithmResult {
        workers = Array(threadNum) { _ -> Worker.start()}
        try {
            return super.run(clusterNum, maxIterations)
        } finally {
            workers.forEach {it.requestTermination().result}
        }
    }

    override fun clusterPoints(centroids: Collection<Vector>, data: Data): Map<Vector, List<Vector>> {
        val chunks = chunk(data.points)

        chunks.freeze()
        centroids.freeze()

        val futures = Array(workers.size) { workerIndex ->
            workers[workerIndex].execute(TransferMode.SAFE, { //split centroid calculation for parallel processing
                Pair(centroids, chunks[workerIndex])
            }) { data -> data.component2().map {Pair(it, closestCentroid(vectorMath, it, data.component1())) }} //assign closes centroids
        }
        val futureSet = futures.toSet()
        val pairs = mutableListOf<List<Pair<Vector, Vector>>>()
        while (pairs.size < futureSet.size) {
            val ready = waitForMultipleFutures(futureSet, 10000)
            pairs.addAll(ready.map{it.consume {result -> result}})  //collect assignments
        }

        return pairs.flatten().groupBy({ it.component2() }, { it.component1() })  //group by centroids
    }

    override fun calculateCenter(points: List<Vector>): Vector {
        val chunks = chunk(points)
        chunks.freeze()

        val futures = Array(workers.size) { workerIndex ->
            workers[workerIndex].execute(TransferMode.SAFE, {
                chunks[workerIndex]
            }) {it.reduce{ v1, v2 -> vectorMath.add(v1, v2) }}  //sum vector values in parallel
        }
        val futureSet = futures.toSet()
        val sum = mutableListOf<Vector>()
        while (sum.size < futureSet.size) {
            val ready = waitForMultipleFutures(futureSet, 10000)
            sum.addAll(ready.map{it.consume {result -> result}}.toList())  //sum intermediate sums
        }

        return vectorMath.divideBy(sum.reduce { v1, v2 -> vectorMath.add(v1, v2) }, points.size) //find average
    }

    private fun chunk(points: List<Vector>):List<List<Vector>> {
        return points.chunked(if (points.size < threadNum) points.size else points.size / threadNum)
    }
}

