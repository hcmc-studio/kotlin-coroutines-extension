package studio.hcmc.kotlin.coroutines

import java.nio.channels.CompletionHandler
import kotlin.coroutines.Continuation

internal class AsynchronousCompletionHandler<T>(
    private val continuation: Continuation<T>
) : CompletionHandler<T, Unit> {
    override fun completed(result: T, attachment: Unit?) {
        continuation.resumeWith(Result.success(result))
    }

    override fun failed(exc: Throwable, attachment: Unit?) {
        continuation.resumeWith(Result.failure(exc))
    }
}

fun <T> Continuation<T>.asynchronousCompletionHandler(): CompletionHandler<T, Unit> {
    return AsynchronousCompletionHandler(this)
}