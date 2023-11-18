package studio.hcmc.kotlin.coroutines

import kotlinx.coroutines.*
import java.util.LinkedList
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private val defaultDelay = 10.milliseconds

private fun Iterable<*>.defaultSize(): Int {
    return if (this is Collection) { size } else { 10 }
}

suspend fun <T> Iterable<T>.parallelForEach(
    maxConcurrency: Int,
    coroutineContext: CoroutineContext,
    delay: Duration = defaultDelay,
    block: suspend (T) -> Unit
) {
    val queue = LinkedList<Job>()
    for (element in this) {
        queue.add(CoroutineScope(coroutineContext).launch {
            block(element)
        })

        while (queue.size >= maxConcurrency) {
            val iterator = queue.listIterator()
            var removed = false
            while (iterator.hasNext()) {
                val job = iterator.next()
                if (job.isCompleted) {
                    removed = true
                }
            }

            if (!removed) {
                delay(delay)
            }
        }
    }

    queue.joinAll()
}

suspend fun <T, R> Iterable<T>.parallelMap(
    maxConcurrency: Int,
    coroutineContext: CoroutineContext,
    delay: Duration = defaultDelay,
    transform: suspend (T) -> R
): List<R> {
    return parallelMapTo(
        destination = ArrayList(defaultSize()),
        maxConcurrency = maxConcurrency,
        coroutineContext = coroutineContext,
        delay = delay,
        transform = transform
    )
}

suspend fun <T, R, C : MutableCollection<R>> Iterable<T>.parallelMapTo(
    destination: C,
    maxConcurrency: Int,
    coroutineContext: CoroutineContext,
    delay: Duration = defaultDelay,
    transform: suspend (T) -> R
): C {
    val queue = LinkedList<Deferred<R>>()
    for (element in this) {
        if (queue.size < maxConcurrency) {
            queue.add(CoroutineScope(coroutineContext).async {
                transform(element)
            })
        } else {
            while (queue.size >= maxConcurrency) {
                val iterator = queue.listIterator()
                var removed = false
                while (iterator.hasNext()) {
                    val deferred = iterator.next()
                    if (deferred.isCompleted) {
                        removed = true
                        destination.add(deferred.getCompleted())
                    }
                }

                if (!removed) {
                    delay(delay)
                }
            }
        }
    }

    while (queue.isNotEmpty()) {
        val deferred = queue.removeFirst()
        destination.add(deferred.await())
    }

    return destination
}

