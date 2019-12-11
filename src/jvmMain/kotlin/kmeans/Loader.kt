package kmeans

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.streams.toList

actual class Loader actual constructor() {

    @Throws(IOException::class)
    actual fun readData(fileName: String, vectorSize: Int, limit: Int): Data {
        val points = Files.lines(Paths.get(fileName))
            .limit(limit.toLong())
            .map { it.split('\t').subList(0, vectorSize)}
            .map { a -> Vector(vectorSize, a.map { it.toDouble() }.toDoubleArray()) }
            .toList()
        return Data(points, vectorSize)
    }
}
