package studio.hcmc.kotlin.coroutines

import java.nio.ByteBuffer
import java.util.LinkedList

internal suspend fun readAllAsync(
    bufferSize: Int = DEFAULT_BUFFER_SIZE,
    read: suspend (dst: ByteBuffer, offset: Long) -> Int
): ByteBuffer {
    if (bufferSize == 0) {
        throw IllegalArgumentException()
    }

    var offset = 0L
    val list = LinkedList<ByteBuffer>()
    while (true) {
        val buffer = ByteBuffer.allocate(bufferSize)
        val size = read(buffer, offset)
        if (size == bufferSize) {
            list.add(buffer)
            offset += size
            continue
        }

        var mergeSize = list.size * bufferSize
        if (size > 0) {
            mergeSize += size
        }
        val merged = ByteBuffer.allocate(mergeSize)
        for ((index, saved) in list.withIndex()) {
            merged.put(index * bufferSize, saved, 0, bufferSize)
        }
        if (size > 0) {
            merged.put(list.size * bufferSize, buffer.array(), 0, size)
        }

        return merged
    }
}