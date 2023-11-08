package studio.hcmc.kotlin.concurrency

import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.datetime.*
import studio.hcmc.kotlin.coroutines.launch
import java.util.Timer
import java.util.concurrent.Executors
import kotlin.concurrent.schedule
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.time.Duration.Companion.days

abstract class Scheduler(
    private val schedule: Schedule
) {
    private val scheduledExecutor = Executors.newScheduledThreadPool(1)
    private val taskExecutor = Executors.newFixedThreadPool(1)
    private val taskDispatcher = taskExecutor.asCoroutineDispatcher()
    private var job: Job? = null
    private val timer = Timer()

    protected abstract suspend operator fun invoke()

    fun start() {
        when (schedule) {
            is FixedDelaySchedule -> start(schedule)
            is FixedRateSchedule -> start(schedule)
            is FixedTimeSchedule -> start(schedule)
        }
    }

    private fun start(schedule: FixedRateSchedule) {
        timer.scheduleAtFixedRate(schedule.initialDelay.inWholeMilliseconds, schedule.period.inWholeMilliseconds) {
            launch()
        }
    }

    private fun start(schedule: FixedDelaySchedule) {
        timer.schedule(schedule.initialDelay.inWholeMilliseconds, schedule.period.inWholeMilliseconds) {
            launch()
        }
    }

    private fun start(schedule: FixedTimeSchedule) {
        timer.scheduleAtFixedRate(schedule.time.atDate(Clock.System.todayIn(TimeZone.UTC)), 1.days.inWholeMilliseconds) {
            launch()
        }
    }

    private fun launch() {
        job = taskDispatcher.launch { this@Scheduler() }
    }

    fun terminate() {

    }
}