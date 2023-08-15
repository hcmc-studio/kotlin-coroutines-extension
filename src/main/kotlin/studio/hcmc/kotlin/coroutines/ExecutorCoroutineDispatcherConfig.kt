package studio.hcmc.kotlin.coroutines

import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.*

interface ExecutorCoroutineDispatcherConfig {
    val corePoolSize: Int
    val maximumPoolSize: Int
    val keepAliveTime: Long
    val timeUnit: TimeUnit
    val workQueue: BlockingQueue<Runnable>
    val threadFactory: ThreadFactory
    val rejectedExecutionHandler: RejectedExecutionHandler

    val description: String get() = "${this::class.simpleName}(" +
            "corePoolSize=$corePoolSize, " +
            "maximumPoolSize=$maximumPoolSize, " +
            "keepAliveTime=$keepAliveTime, " +
            "timeUnit=${timeUnit.name}, " +
            "workQueue=${workQueue::class.simpleName}, " +
            "threadFactory=${threadFactory::class.simpleName ?: "<anonymous>"}, " +
            "rejectedExecutionHandler=${rejectedExecutionHandler::class.simpleName}" +
            ")"

    data class Builder(
        override var corePoolSize: Int = 0,
        override var maximumPoolSize: Int = Int.MAX_VALUE,
        override var keepAliveTime: Long = 60L,
        override var timeUnit: TimeUnit = TimeUnit.SECONDS,
        override var workQueue: BlockingQueue<Runnable> = SynchronousQueue(),
        override var threadFactory: ThreadFactory = Executors.defaultThreadFactory(),
        override var rejectedExecutionHandler: RejectedExecutionHandler = ThreadPoolExecutor.AbortPolicy()
    ) : ExecutorCoroutineDispatcherConfig

    companion object {
        operator fun invoke(configuration: Builder.() -> Unit): ExecutorCoroutineDispatcherConfig {
            return Builder().apply(configuration)
        }

        operator fun invoke(default: ExecutorCoroutineDispatcherConfig, configuration: Builder.() -> Unit): ExecutorCoroutineDispatcherConfig {
            return Builder()
                .apply {
                    this.corePoolSize = default.corePoolSize
                    this.maximumPoolSize = default.maximumPoolSize
                    this.keepAliveTime = default.keepAliveTime
                    this.timeUnit = default.timeUnit
                    this.workQueue = default.workQueue
                    this.threadFactory = default.threadFactory
                    this.rejectedExecutionHandler = default.rejectedExecutionHandler
                }
                .apply(configuration)
        }
    }
}

fun ExecutorCoroutineDispatcherConfig.Companion.fixed(nThreads: Int): ExecutorCoroutineDispatcherConfig {
    return ExecutorCoroutineDispatcherConfig {
        this.corePoolSize = nThreads
        this.maximumPoolSize = nThreads
        this.keepAliveTime = 0L
        this.timeUnit = TimeUnit.MILLISECONDS
        this.workQueue = LinkedBlockingQueue()
    }
}

fun ExecutorCoroutineDispatcherConfig.Companion.single(): ExecutorCoroutineDispatcherConfig {
    return ExecutorCoroutineDispatcherConfig {
        this.corePoolSize = 1
        this.maximumPoolSize = 1
        this.keepAliveTime = 0L
        this.timeUnit = TimeUnit.MILLISECONDS
        this.workQueue = LinkedBlockingQueue()
    }
}

fun ExecutorCoroutineDispatcherConfig.Companion.cached(): ExecutorCoroutineDispatcherConfig {
    return ExecutorCoroutineDispatcherConfig {
        this.corePoolSize = 0
        this.maximumPoolSize = Int.MAX_VALUE
        this.keepAliveTime = 60L
        this.timeUnit = TimeUnit.MINUTES
        this.workQueue = SynchronousQueue()
    }
}

fun ExecutorCoroutineDispatcherConfig.createExecutorService(): ThreadPoolExecutor {
    return ThreadPoolExecutor(
        /* corePoolSize = */    corePoolSize,
        /* maximumPoolSize = */ maximumPoolSize,
        /* keepAliveTime = */   keepAliveTime,
        /* unit = */            timeUnit,
        /* workQueue = */       workQueue,
        /* threadFactory = */   threadFactory,
        /* handler = */         rejectedExecutionHandler
    )
}

fun ExecutorCoroutineDispatcherConfig.createCoroutineDispatcher(): ExecutorCoroutineDispatcher {
    return createExecutorService().asCoroutineDispatcher()
}