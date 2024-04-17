package com.example.socketconnectiontest

import android.sax.EndTextElementListener
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class SocketManager(private val ip: String, private val port: Int) {
    private var socket: Socket? = null

    fun connect(listener: SocketConnectionListener) {
        Thread {
            try {
                socket = Socket(ip, port)
                val input = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                val output = PrintWriter(socket!!.getOutputStream(), true)

                // Do any necessary operations here after connection

                // Notify the listener that connection is successful
                listener.onConnected()
            } catch (e: Exception) {
                // Notify the listener about connection failure
                listener.onConnectionFailed(e.message ?: "Unknown error")
            }
        }.start()
    }

    fun disconnect() {
        try {
            socket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    interface SocketConnectionListener {
        fun onConnected()
        fun onConnectionFailed(errorMessage: String)
    }
}