package studio.hcmc.kotlin.coroutines

import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

private fun Iterable<*>.defaultSize(): Int {
    return if (this is Collection) { size } else { 10 }
}

suspend fun <T> Iterable<T>.parallelForEach(
    maxConcurrency: Int,
    coroutineContext: CoroutineContext,
    block: suspend (T) -> Unit
) {
    val queue = LinkedList<Job>()
    for (element in this) {
        queue.add(CoroutineScope(coroutineContext).launch {
            block(element)
        })

        if (queue.size >= maxConcurrency) {
            queue.removeFirst().join()
        }
    }

    queue.joinAll()
}

suspend fun <T, R> Iterable<T>.parallelMap(
    maxConcurrency: Int,
    coroutineContext: CoroutineContext,
    transform: suspend (T) -> R
): List<R> {
    return parallelMapTo(
        destination = ArrayList(defaultSize()),
        maxConcurrency = maxConcurrency,
        coroutineContext = coroutineContext,
        transform = transform
    )
}

suspend fun <T, R, C : MutableCollection<R>> Iterable<T>.parallelMapTo(
    destination: C,
    maxConcurrency: Int,
    coroutineContext: CoroutineContext,
    transform: suspend (T) -> R
): C {
    val queue = LinkedList<Deferred<R>>()
    for (element in this) {
        queue.add(CoroutineScope(coroutineContext).async {
            transform(element)
        })

        if (queue.size >= maxConcurrency) {
            destination.add(queue.removeFirst().await())
        }
    }

    destination.addAll(queue.map { it.await() })

    return destination
}

