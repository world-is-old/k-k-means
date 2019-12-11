package kmeans

expect class Loader() {
    fun readData(fileName: String, vectorSize: Int, limit: Int): Data
}