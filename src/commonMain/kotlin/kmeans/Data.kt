package kmeans

import math.Vector

class Data (val points: List<Vector>, val vectorSize: Int) {

    init {
        points.forEach { require(it.size == vectorSize) {"Vector size is invalid: ${it.size}, ${it.values}"}}
    }
}