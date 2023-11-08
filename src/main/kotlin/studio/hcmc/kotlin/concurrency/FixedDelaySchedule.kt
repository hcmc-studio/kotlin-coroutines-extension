package studio.hcmc.kotlin.concurrency

import kotlin.time.Duration

data class FixedDelaySchedule(
    val initialDelay: Duration,
    val period: Duration
) : Schedule
