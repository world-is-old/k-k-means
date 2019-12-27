import kmeans.*
import math.VectorMath
import kotlin.random.Random

class Runner {
    fun run(mode: String, vectorMath: VectorMath) {
        val vectorSize = 512
        val clusterNum = 10
        val maxIterations = 10
        val limit = 100000

        println("Reading file $mode")
        val readTimer = Timer()

        val data = Loader().readData("data/long.csv", vectorSize, limit, vectorMath)

        println("Loaded ${data.points.size} records, in ${readTimer.elapsedSec()} seconds \n ---------------------")

        println("Calculating")
        var random = Random(100)
        run(KMeans(data, vectorMath, random), clusterNum, maxIterations)

        random = Random(100)
        run( KMeansParallel(data, vectorMath,random,1), clusterNum, maxIterations)

        random = Random(100)
        run( KMeansParallel(data, vectorMath,random,2), clusterNum, maxIterations)

        random = Random(100)
        run( KMeansParallel(data, vectorMath,random,4), clusterNum, maxIterations)

        random = Random(100)
        run( KMeansParallel(data, vectorMath,random,40), clusterNum, maxIterations)
    }

    fun run(algorithm: KMeans, clusterNum: Int, maxIterations: Int) {
        val runTimer = Timer()
        val result = algorithm.run(clusterNum, maxIterations)
        println("Done in ${runTimer.elapsedSec()} seconds, ${result.iterations} iterations \n ---------------------")
    }
}