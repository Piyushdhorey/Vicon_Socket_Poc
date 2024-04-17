package com.example.socketconnectiontest

import org.greenrobot.eventbus.EventBus

class ConnectionEvent(val connected: Boolean)

object EventBusSingleton {
    fun postConnectionEvent(connected: Boolean) {
        EventBus.getDefault().post(ConnectionEvent(connected))
    }
}