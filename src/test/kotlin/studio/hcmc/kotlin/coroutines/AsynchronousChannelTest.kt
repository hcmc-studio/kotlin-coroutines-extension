package studio.hcmc.kotlin.coroutines

import kotlinx.coroutines.runBlocking
import java.nio.channels.AsynchronousFileChannel
import kotlin.io.path.Path
import kotlin.test.Test

class AsynchronousChannelTest {
    @Test
    fun readAllAsyncTest(): Unit = runBlocking {
        val path = Path("test.txt")
        val buffer = AsynchronousFileChannel
            .open(path)
            .readAllAsync(bufferSize = 5)
//            .readAllAsync()
        println(String(buffer.array()))
    }
}