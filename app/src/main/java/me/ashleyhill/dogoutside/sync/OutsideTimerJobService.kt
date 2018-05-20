package me.ashleyhill.dogoutside.sync

import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import me.ashleyhill.dogoutside.data.DogOutsidePreferences
import java.util.concurrent.TimeUnit

class OutsideTimerJobService: JobService() {

    companion object {
        private val TAG = OutsideTimerJobService::class.java.simpleName

        const val JOB_ID: Int = 2000
        private val TIMER_JOB_INTERVAL: Long = TimeUnit.MINUTES.toMillis(1)

        fun schedule(context: Context) {
            val componentName = ComponentName(context, OutsideTimerJobService::class.java)
            val jobScheduler: JobScheduler = (context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler)

            val builder = JobInfo.Builder(JOB_ID, componentName)
                    .setPeriodic(TIMER_JOB_INTERVAL)

            jobScheduler.schedule(builder.build())
        }
    }
    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job Over")
        stopService(Intent(this.applicationContext, OutsideTimerService::class.java))

        return false
    }

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "Job Started")
        doWork(params)
        return true
    }

    private fun doWork(params: JobParameters?) {
        val context = this.applicationContext

        Log.d(TAG, "Job Working")

        startService(Intent(context, OutsideTimerService::class.java))
        jobFinished(params, DogOutsidePreferences.getDogOutside(context))
    }
}
