import kmeans.KMeans
import kmeans.KMeansParallel
import kmeans.Loader
import kmeans.Timer
import kotlin.random.Random

class Runner {
    fun run(mode: String) {
        val vectorSize = 512
        val clusterNum = 2
        val maxIterations = 10
        val limit = 10000

        println("Reading file $mode")
        val readTimer = Timer()

        val data = Loader().readData("data/data.csv", vectorSize, limit)

        println("Loaded ${data.points.size} records, in ${readTimer.elapsedSec()} seconds \n ---------------------")

        println("Calculating")
        var random = Random(100)

        run(KMeans(data, random), clusterNum, maxIterations)

        random = Random(100)
        run( KMeansParallel(data, random,1), clusterNum, maxIterations)

        random = Random(100)
        run( KMeansParallel(data, random,2), clusterNum, maxIterations)

        random = Random(100)
        run( KMeansParallel(data, random,4), clusterNum, maxIterations)

        random = Random(100)
        run( KMeansParallel(data, random,40), clusterNum, maxIterations)
    }

    fun run(algorithm: KMeans, clusterNum: Int, maxIterations: Int) {
        val runTimer = Timer()
        val result = algorithm.run(clusterNum, maxIterations)
        println("Done in ${runTimer.elapsedSec()} seconds, ${result.iterations} iterations \n ---------------------")
    }
}
