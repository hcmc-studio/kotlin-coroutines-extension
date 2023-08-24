package studio.hcmc.kotlin.coroutines

import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.AsynchronousSocketChannel
import java.util.concurrent.TimeUnit
import kotlin.coroutines.suspendCoroutine

suspend fun AsynchronousSocketChannel.connectAsync(
    remote: SocketAddress
): Void {
    return suspendCoroutine {
        connect(remote, Unit, AsynchronousCompletionHandler(it))
    }
}

suspend fun AsynchronousSocketChannel.readAsync(
    dst: ByteBuffer,
    timeout: Long = 0L,
    unit: TimeUnit = TimeUnit.MILLISECONDS
): Int {
    return suspendCoroutine {
        read(dst, timeout, unit, Unit, AsynchronousCompletionHandler(it))
    }
}

suspend fun AsynchronousSocketChannel.readAsync(
    dsts: Array<ByteBuffer>,
    offset: Int,
    length: Int,
    timeout: Long = 0L,
    unit: TimeUnit = TimeUnit.MILLISECONDS
): Long {
    return suspendCoroutine {
        read(dsts, offset, length, timeout, unit, Unit, AsynchronousCompletionHandler(it))
    }
}

suspend fun AsynchronousSocketChannel.writeAsync(
    src: ByteBuffer,
    timeout: Long = 0L,
    unit: TimeUnit = TimeUnit.MILLISECONDS
): Int {
    return suspendCoroutine {
        write(src, timeout, unit, Unit, AsynchronousCompletionHandler(it))
    }
}

suspend fun AsynchronousSocketChannel.writeAsync(
    srcs: Array<ByteBuffer>,
    offset: Int,
    length: Int,
    timeout: Long = 0L,
    unit: TimeUnit = TimeUnit.MILLISECONDS
): Long {
    return suspendCoroutine {
        write(srcs, offset, length, timeout, unit, Unit, AsynchronousCompletionHandler(it))
    }
}