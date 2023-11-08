package studio.hcmc.kotlin.concurrency

import kotlin.time.Duration

/**
 * 작업의 시작 시각을 기준으로 [period]만큼 지난 후 작업을 반복합니다. 작업의 지연 시간은 고려하지 않습니다.
 */
data class FixedRateSchedule(
    val initialDelay: Duration,
    val period: Duration,
) : Schedule
