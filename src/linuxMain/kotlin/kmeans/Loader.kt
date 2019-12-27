package kmeans

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toKString
import math.Vector
import math.VectorMath
import platform.posix.*

actual class Loader {

    actual fun readData(fileName: String, vectorSize: Int, limit: Int, vectorMath: VectorMath): Data {
        val file = fopen(fileName, "r")
        if (file == null) {
            perror("cannot open input file $fileName")
            throw Exception("Cannot open input file $fileName")
        }

        if (setvbuf(file, null, _IOFBF, 0xFFFF) != 0) {
            perror("cannot set buffer to 0xFFFF bytes")
            throw Exception("cannot set buffer to 0xFFFF bytes")
        }


        val records = mutableListOf<Vector>()
        try {
            memScoped {
                val bufferLength = 512 * 32
                val buffer = allocArray<ByteVar>(bufferLength)

                do {
                    val nextLine = fgets(buffer, bufferLength, file)?.toKString()
                    if (nextLine == null || nextLine.isEmpty() || records.size >= limit) {
                        println("no more lines")
                        break
                    }

                    val tokens = nextLine.split('\t').subList(0, vectorSize)
                    val vector = vectorMath.fromDoubles(tokens.map { it.toDouble() }.toDoubleArray())
                    records.add(vector)
                } while (true)
            }
        } finally {
            fclose(file)
        }
        return Data(records, vectorSize)

//        val file = File(fileName)
//        val reader = FileInputStream(file).utf8Reader()
//        reader.use {
//            val text =  reader.readText().trim()
//            val records = text.splitToSequence('\n')
//                .map{it.split('\t')}
//                .map{tokens -> Vector(vectorSize, tokens.subList(0, vectorSize).map { it.toDouble()}.toDoubleArray())}
//                .toList()

//            val records = mutableListOf<Vector>()
//            do {
//                try {
//                    val nextLine = reader.readln()
//                    if (nextLine.isEmpty() || records.size >= limit) {
//                        break
//                    }
//
//                    val tokens = nextLine.split('\t').subList(0, vectorSize)
//                    val vector = Vector(vectorSize, tokens.subList(0, vectorSize).map { it.toDouble() }.toDoubleArray())
//                    records.add(vector)
//                } catch (e: EOFException) {
//                    println("read ${records.size} lines" )
//                }
//            } while (true)
//            return Data(records, vectorSize)
//        }
    }
}
