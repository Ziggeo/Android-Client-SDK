package com.ziggeo.androidsdk.demo.model.system.message

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable


/**
 * Created by Alexander Bedulin on 25-Sep-19.
 * Ziggeo, Inc.
 * alexb@ziggeo.com
 */
class SystemMessageNotifier {
    private val notifierRelay = PublishRelay.create<SystemMessage>()

    val notifier: Observable<SystemMessage> = notifierRelay.hide()
    fun send(message: SystemMessage) = notifierRelay.accept(message)
    fun send(message: String) = notifierRelay.accept(SystemMessage(message))
}