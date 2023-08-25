package studio.hcmc.kotlin.coroutines

import kotlinx.coroutines.suspendCancellableCoroutine
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousByteChannel

/**
 * @see AsynchronousByteChannel.read
 */
suspend fun AsynchronousByteChannel.readAsync(dst: ByteBuffer): Int {
    return suspendCancellableCoroutine {
        read(dst, Unit, it.asynchronousCompletionHandler())
    }
}

/**
 * @throws IllegalArgumentException [bufferSize]가 0일 때
 */
suspend fun AsynchronousByteChannel.readAllAsync(bufferSize: Int = DEFAULT_BUFFER_SIZE): ByteBuffer {
    return readAllAsync(bufferSize) { readAsync(it) }
}

suspend fun AsynchronousByteChannel.writeAsync(src: ByteBuffer): Int {
    return suspendCancellableCoroutine {
        write(src, Unit, it.asynchronousCompletionHandler())
    }
}