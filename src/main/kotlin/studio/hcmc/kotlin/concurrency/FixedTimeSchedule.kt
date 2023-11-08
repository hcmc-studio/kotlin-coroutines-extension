package studio.hcmc.kotlin.concurrency

import kotlinx.datetime.LocalTime

data class FixedTimeSchedule(
    val time: LocalTime
) : Schedule
