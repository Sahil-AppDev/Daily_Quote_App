package com.example.dailyquoteapp.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dailyquoteapp.R
import com.example.dailyquoteapp.repository.QuoteRepository
import kotlinx.coroutines.runBlocking

class DailyQuoteWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        return try {
            val quote = runBlocking {
                QuoteRepository().fetchQuotes().random()
            }

            val notification = NotificationCompat.Builder(
                applicationContext,
                NotificationUtils.CHANNEL_ID
            )
                .setSmallIcon(R.drawable.notification)
                .setContentTitle("Quote of the Day ✨")
                .setContentText("“${quote.content}” — ${quote.author}")
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .bigText("“${quote.content}”\n\n— ${quote.author}")
                )
                .setAutoCancel(true)
                .build()

            NotificationManagerCompat.from(applicationContext)
                .notify(1001, notification)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
