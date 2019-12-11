package kmeans

import kotlin.random.Random

expect class KMeansParallel(data: Data, random: Random, threadNum: Int): KMeans