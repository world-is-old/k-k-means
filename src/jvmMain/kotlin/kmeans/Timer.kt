package kmeans

actual class Timer {
    private var startTime = System.currentTimeMillis()

    actual fun elapsedSec(): Float {
        return (System.currentTimeMillis() - startTime).toFloat() / 1000
    }

    actual fun reset() {
        startTime = System.currentTimeMillis()
    }
}