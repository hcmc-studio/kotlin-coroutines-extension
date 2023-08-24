package studio.hcmc.kotlin.coroutines

import java.nio.channels.AsynchronousServerSocketChannel
import java.nio.channels.AsynchronousSocketChannel
import kotlin.coroutines.suspendCoroutine

suspend fun AsynchronousServerSocketChannel.acceptAsync(): AsynchronousSocketChannel {
    return suspendCoroutine {
        accept(Unit, AsynchronousCompletionHandler(it))
    }
}