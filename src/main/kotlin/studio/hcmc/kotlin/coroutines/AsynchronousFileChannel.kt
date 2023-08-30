package studio.hcmc.kotlin.coroutines

import kotlinx.coroutines.suspendCancellableCoroutine
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousFileChannel
import java.nio.channels.CompletionHandler
import java.nio.channels.FileLock
import kotlin.coroutines.suspendCoroutine

suspend fun AsynchronousFileChannel.lockAsync(
    position: Long = 0L,
    size: Long = Long.MAX_VALUE,
    shared: Boolean = false
): FileLock {
    return suspendCancellableCoroutine {
        lock(position, size, shared, Unit, AsynchronousCompletionHandler(it))
    }
}

suspend fun AsynchronousFileChannel.readAsync(
    dst: ByteBuffer,
    position: Long = 0L
): Int {
    return suspendCoroutine {
        read(dst, position, Unit, AsynchronousCompletionHandler(it))
    }
}

suspend fun AsynchronousFileChannel.readAllAsync(bufferSize: Int = DEFAULT_BUFFER_SIZE): ByteBuffer {
    return readAllAsync(bufferSize) { dst, offset -> readAsync(dst, offset) }
}

suspend fun AsynchronousFileChannel.writeAsync(
    src: ByteBuffer,
    position: Long = 0L
): Int {
    return suspendCoroutine {
        write(src, position, Unit, AsynchronousCompletionHandler(it))
    }
}