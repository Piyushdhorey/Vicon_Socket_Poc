package com.example.socketconnectiontest

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.ghgande.j2mod.modbus.ModbusException
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.Socket

class PlcService: Service() {

    private lateinit var modbusMaster: ModbusTCPMaster
    private var isConnected = false
    private lateinit var socket: Socket
    private lateinit var reader: BufferedReader
    private lateinit var writer: BufferedWriter

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val ipAddress = intent?.getStringExtra("IP_ADDRESS") ?: ""
        val port = intent?.getIntExtra("PORT", 0) ?: 0

        GlobalScope.launch(Dispatchers.IO) {
            modbusMaster = ModbusTCPMaster(ipAddress, port)
            try {
                modbusMaster.connect()
                isConnected = true
//                socket = Socket(ipAddress, port)
                Log.d(TAG, "Connected to PLC")
//
//                reader = BufferedReader(InputStreamReader(socket.getInputStream()))
//                writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
                val loginIntent = Intent(this@PlcService, LoginActivity::class.java)
                loginIntent.putExtra("IP_ADDRESS", ipAddress)
                loginIntent.putExtra("PORT", port)
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(loginIntent)


                EventBusSingleton.postConnectionEvent(true)
            } catch (e: ModbusException) {
                Log.e(TAG, "Failed to connect to PLC", e)
                EventBusSingleton.postConnectionEvent(false)
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isConnected) {
            modbusMaster.disconnect()
//            writer.close()
//            reader.close()
//            socket.close()
        }
    }

    companion object {
        private const val TAG = "PLCService"
    }
}