package studio.hcmc.kotlin.coroutines

import java.nio.ByteBuffer

internal suspend fun readAllAsync(
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    read: suspend (dst: ByteBuffer) -> Int
): ByteBuffer {
    if (bufferSize == 0) {
        throw IllegalArgumentException()
    }

    val buffers = ArrayList<ByteBuffer>()
    while (true) {
        val buffer = ByteBuffer.allocate(bufferSize)
        val size = read(buffer)
        if (size == -1 || size != bufferSize) {
            var mergeSize = buffers.size * bufferSize
            if (size > 0) {
                mergeSize += size
            }

            val merged = ByteBuffer.allocate(mergeSize)
            for (buffer in buffers) {
                merged.put(buffer)
            }

            if (size > 0) {
                merged.put(buffer)
            }

            return merged
        }

        buffers.add(buffer)
    }
}