package kmeans

import math.VectorMath

expect class Loader() {
    fun readData(fileName: String, vectorSize:Int, limit: Int, vectorMath: VectorMath): Data
}