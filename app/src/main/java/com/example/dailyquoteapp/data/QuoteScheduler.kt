package com.example.dailyquoteapp.data


import android.content.Context
import androidx.work.*
import com.example.dailyquoteapp.utils.DailyQuoteWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

object QuoteScheduler {

    fun scheduleDailyQuote(
        context: Context,
        hour: Int,
        minute: Int
    ) {
        val now = Calendar.getInstance()
        val scheduled = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)

            if (before(now)) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val delay =
            scheduled.timeInMillis - now.timeInMillis

        val work = OneTimeWorkRequestBuilder<DailyQuoteWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "daily_quote_work",
                ExistingWorkPolicy.REPLACE,
                work
            )
    }
}
