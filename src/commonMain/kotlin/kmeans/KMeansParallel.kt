package kmeans

import math.VectorMath
import kotlin.random.Random

expect class KMeansParallel(data: Data, vectorMath: VectorMath, random: Random, threadNum: Int): KMeans