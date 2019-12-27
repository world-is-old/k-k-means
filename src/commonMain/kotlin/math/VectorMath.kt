package math

import kotlin.random.Random

interface VectorMath {

    fun zero(size: Int): Vector

    fun fromDoubles(values: DoubleArray): Vector

    fun minMaxBound(vectors: List<Vector>, vectorSize: Int): Pair<Vector, Vector>

    fun add(v1: Vector, v2: Vector): Vector

    fun distance(v1: Vector, v2: Vector): Double

    fun distMeasure(v1: Vector, v2: Vector): Double

    fun divideBy(v: Vector, d: Int): Vector

    fun rand(random: Random, min: Vector, max: Vector): Vector
}