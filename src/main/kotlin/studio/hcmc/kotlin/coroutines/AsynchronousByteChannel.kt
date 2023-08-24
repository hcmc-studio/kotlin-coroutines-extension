package studio.hcmc.kotlin.coroutines

import kotlinx.coroutines.suspendCancellableCoroutine
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousByteChannel

suspend fun AsynchronousByteChannel.readAsync(dst: ByteBuffer): Int {
    return suspendCancellableCoroutine {
        read(dst, Unit, it.asynchronousCompletionHandler())
    }
}

suspend fun AsynchronousByteChannel.writeAsync(src: ByteBuffer): Int {
    return suspendCancellableCoroutine {
        write(src, Unit, it.asynchronousCompletionHandler())
    }
}