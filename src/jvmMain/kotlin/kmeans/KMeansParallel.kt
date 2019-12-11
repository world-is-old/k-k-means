package kmeans

import kotlinx.coroutines.*
import java.util.concurrent.Executors
import kotlin.random.Random

actual class KMeansParallel actual constructor(data: Data, random: Random, val threadNum: Int): KMeans(data, random) {

    lateinit var dispatcher: ExecutorCoroutineDispatcher

    override fun run(clusterNum: Int, maxIterations: Int): AlgorithmResult {
        dispatcher = Executors.newFixedThreadPool(threadNum).asCoroutineDispatcher()
        dispatcher.use {
            return super.run(clusterNum, maxIterations)
        }
    }

    override fun clusterPoints(centroids: Collection<Vector>, data: Data): Map<Vector, List<Vector>> {
        val chunkSize: Int = data.points.size / threadNum
        val chunks = data.points.chunked(chunkSize)

        return runBlocking (dispatcher) {
            chunks.map { chunk ->
                 async {
                     chunk.map { point -> Pair(closestCentroid(point, centroids), point) }
                 }
            }.map {it.await()}
             .flatten()
             .groupBy({ it.component1() }, { it.component2() })
        }
    }

    override fun calculateCenter(points: List<Vector>): Vector {
        val chunks = points.chunked(if (points.size < threadNum) points.size else points.size / threadNum)
        val sum = runBlocking (dispatcher) {
            chunks.map { chunk ->
                async {
                    chunk.reduce{ v1, v2 -> Vectors.add(v1, v2) }
                }
            }.map {it.await()}
                .reduce{v1, v2 -> Vectors.add(v1, v2) }
        }

        return Vectors.divideBy(sum, points.size)
    }

    override fun spawnCenters(clusterNum: Int, data: Data): List<Vector> {
        return runBlocking (dispatcher) {
            (0 until clusterNum).map {
                async {
                    data.randWithin(random)
                }
            }.map{it.await()}
            .toList()
        }
    }
}
