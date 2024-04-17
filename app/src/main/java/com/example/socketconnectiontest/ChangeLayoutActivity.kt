package com.example.socketconnectiontest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.Socket

class ChangeLayoutActivity : AppCompatActivity() {

    private lateinit var btnSendCommand: Button
    private lateinit var ipAddress: String
    private var port: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_layout)

        ipAddress = intent.getStringExtra("IP_ADDRESS") ?: ""
        port = intent.getIntExtra("PORT", 0)

        btnSendCommand = findViewById(R.id.sendCommand)

        btnSendCommand.setOnClickListener {
            sendCommandToPLC()
        }
    }

    private fun sendCommandToPLC() {
        GlobalScope.launch(Dispatchers.IO) {
            val socket = Socket(ipAddress, port)

            try {
                val writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                val command =
                    "<PLC_VICON><Change_Rectangle_Mode SEQUENCE=\"nnn\"><NUMBER_OF_DISPLAYED_RECTANGLE>1</NUMBER_OF_DISPLAYED_RECTANGLE></Change_Rectangle_Mode></PLC_VICON>"
                writer.write(command)
                writer.flush()
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle connection or write errors
            } finally {
                socket.close()
            }
        }
    }
}