package com.ziggeo.androidsdk.demo.di.module

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.ziggeo.androidsdk.IZiggeo
import com.ziggeo.androidsdk.Ziggeo
import com.ziggeo.androidsdk.callbacks.RecorderCallback
import com.ziggeo.androidsdk.demo.R
import com.ziggeo.androidsdk.demo.model.data.storage.Prefs
import com.ziggeo.androidsdk.demo.ui.log.EventLogger
import com.ziggeo.androidsdk.net.services.streams.IStreamsServiceRx
import com.ziggeo.androidsdk.net.services.videos.IVideosServiceRx
import com.ziggeo.androidsdk.recorder.MicSoundLevel
import toothpick.config.Module


/**
 * Created by Alexander Bedulin on 25-Sep-19.
 * Ziggeo, Inc.
 * alexb@ziggeo.com
 */
class FragmentModule(context: Context, prefs: Prefs, logger: EventLogger) : Module() {

    init {
        val token = prefs.appToken!!
        val ziggeo = Ziggeo(token, context)
        bind(IZiggeo::class.java).toInstance(ziggeo)
        bind(IVideosServiceRx::class.java).toInstance(ziggeo.apiRx().videos())
        bind(IStreamsServiceRx::class.java).toInstance(ziggeo.apiRx().streams())
        bindPrefs(ziggeo, prefs)

        val analytics = FirebaseAnalytics.getInstance(context)
        analytics.setUserId(token)
        bind(FirebaseAnalytics::class.java).toInstance(analytics)

        initRecorderCallback(ziggeo, logger)
    }

    fun bindPrefs(ziggeo: Ziggeo, prefs: Prefs) {
        ziggeo.recorderConfig.startDelay = prefs.startDelay
    }

    private fun initRecorderCallback(ziggeo: IZiggeo, logger: EventLogger) {
        ziggeo.recorderConfig.callback = object : RecorderCallback() {
            override fun loaded() {
                super.loaded()
                logger.addEvent(R.string.ev_rec_loaded)
            }

            override fun manuallySubmitted() {
                super.manuallySubmitted()
                logger.addEvent(R.string.ev_rec_manuallySubmitted)
            }

            override fun recordingStarted() {
                super.recordingStarted()
                logger.addEvent(R.string.ev_rec_recordingStarted)
            }

            override fun recordingStopped(path: String) {
                super.recordingStopped(path)
                logger.addEvent(R.string.ev_rec_recordingStopped, path)
            }

            override fun countdown(timeLeft: Int) {
                super.countdown(timeLeft)
                logger.addEvent(R.string.ev_rec_countdown, timeLeft.toString())
            }

            override fun recordingProgress(time: Long) {
                super.recordingProgress(time)
                logger.addEvent(R.string.ev_rec_recordingProgress, time.toString())
            }

            override fun readyToRecord() {
                super.readyToRecord()
                logger.addEvent(R.string.ev_rec_readyToRecord)
            }

            override fun accessForbidden(permissions: MutableList<String>) {
                super.accessForbidden(permissions)
                logger.addEvent(R.string.ev_rec_accessForbidden, permissions.toString())
            }

            override fun accessGranted() {
                super.accessGranted()
                logger.addEvent(R.string.ev_rec_accessGranted)
            }

            override fun noCamera() {
                super.noCamera()
                logger.addEvent(R.string.ev_rec_noCamera)
            }

            override fun noMicrophone() {
                super.noMicrophone()
                logger.addEvent(R.string.ev_rec_noMicrophone)
            }

            override fun hasCamera() {
                super.hasCamera()
                logger.addEvent(R.string.ev_rec_hasCamera)
            }

            override fun hasMicrophone() {
                super.hasMicrophone()
                logger.addEvent(R.string.ev_rec_hasMicrophone)
            }

            override fun microphoneHealth(micStatus: MicSoundLevel) {
                super.microphoneHealth(micStatus)
                logger.addEvent(R.string.ev_rec_microphoneHealth, micStatus.name)
            }

            override fun canceledByUser() {
                super.canceledByUser()
                logger.addEvent(R.string.ev_rec_canceledByUser)
            }

            override fun error(throwable: Throwable) {
                super.error(throwable)
                logger.addEvent(R.string.ev_rec_error, throwable.toString())
            }

            override fun streamingStarted() {
                super.streamingStarted()
                logger.addEvent(R.string.ev_rec_streamingStarted)
            }

            override fun streamingStopped() {
                super.streamingStopped()
                logger.addEvent(R.string.ev_rec_streamingStopped)
            }
        }
    }
}