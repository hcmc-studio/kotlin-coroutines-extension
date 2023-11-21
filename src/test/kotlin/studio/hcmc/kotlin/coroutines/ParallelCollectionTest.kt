package studio.hcmc.kotlin.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.test.Test

class ParallelCollectionTest {
    private class InfiniteIterable : Iterable<Long> {
        var value = 0L

        private inner class InfiniteIterator : Iterator<Long> {
            override fun hasNext(): Boolean {
                return true
            }

            override fun next(): Long {
                return value++
            }
        }

        override fun iterator(): Iterator<Long> {
            return InfiniteIterator()
        }
    }

    @Test
    fun parallelForEachTest() {
        val iterable = InfiniteIterable()
        var sum = 0L
        val job = CoroutineScope(Dispatchers.IO).launch {
            iterable.parallelForEach(10, Dispatchers.IO) {
                delay(10)
                sum += it
            }
        }

        Thread.sleep(10_000)
        job.cancel()
        println(sum)
        println(iterable.value)
    }

    @Test
    fun forEachTest() {
        val iterable = InfiniteIterable()
        var sum = 0L
        val job = CoroutineScope(Dispatchers.IO).launch {
            iterable.forEach {
                delay(10)
                sum += it
            }
        }

        Thread.sleep(10_000)
        job.cancel()
        println(sum)
        println(iterable.value)
    }
}