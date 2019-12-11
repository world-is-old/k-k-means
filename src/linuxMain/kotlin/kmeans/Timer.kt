package kmeans

import kotlin.system.getTimeMillis

actual class Timer {
    private var startTime = getTimeMillis()

    actual fun elapsedSec(): Float {
        return (getTimeMillis() - startTime).toFloat() / 1000
    }

    actual fun reset() {
        startTime = getTimeMillis()
    }
}