package com.example.socketconnectiontest

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.socketconnectiontest.databinding.ActivityMainBinding
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster
import kotlinx.coroutines.DelicateCoroutinesApi
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {

//    private lateinit var binding: ActivityMainBinding
//    private lateinit var socketManager: SocketManager


//    private val handler = Handler(Looper.getMainLooper())

    private lateinit var txtServerIp: EditText
    private lateinit var txtServerPort: EditText
    private lateinit var btnConnect: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtServerIp = findViewById(R.id.txtServerIP)
        txtServerPort = findViewById(R.id.txtServerPort)
        btnConnect = findViewById(R.id.btnConnect)

        btnConnect.setOnClickListener {
            val ipAddress = txtServerIp.text.toString()
            val port = txtServerPort.text.toString().toIntOrNull()

            if (ipAddress.isNotEmpty() && port != null) {
                val serviceIntent = Intent(this, PlcService::class.java)
                serviceIntent.putExtra("IP_ADDRESS", ipAddress)
                serviceIntent.putExtra("PORT", port)
                startService(serviceIntent)
            }else {
                Toast.makeText(this, "Please enter valid IP address and port", Toast.LENGTH_SHORT).show()
            }
        }


//        socketManager = SocketManager("150.129.157.83", 3560)
//
//        binding.btnConnect.setOnClickListener {
//
//            socketManager.connect(this)
//        }

//        val serverIp: String = "150.129.157.83"
//        val serverPort: Int = 3560
//        val messageToSend = "<PLC_VICON><INITIALIZE SEQUENCE=\"nnn\"><USER_NAME>ADMIN</USER_NAME><PASSWORD>1234</PASSWORD></INITIALIZE></PLC_VICON>"
//
//        binding.btnConnect.setOnClickListener {
//            val serverIp = binding.txtServerIP.text.toString()
//            val serverPort = binding.txtServerPort.text.toString().toIntOrNull()
//
//            if (serverIp.isEmpty() || serverPort == null) {
//                Toast.makeText(this, "Please enter the valid IP and Port", Toast.LENGTH_SHORT).show()
//            }
//
//            GlobalScope.launch(Dispatchers.IO) {
//                try {
//                    val socket = Socket(serverIp, serverPort!!)
//
//                    val writer = OutputStreamWriter(socket.getOutputStream())
//                    writer.write(messageToSend)
//                    writer.flush()
//
//                    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
//                    val receivedResponse = reader.readLine()
//
//                    launch(Dispatchers.Main) {
////                        binding.txtViewReponse.text = "Server Response: $receivedResponse"
//                        binding.viewSocket.visibility = View.GONE
//                        binding.viewLogin.visibility = View.VISIBLE
//                        Log.d("Server Response", receivedResponse)
//                    }
//
//                    socket.close()
//                }catch (e: Exception){
//                    launch(Dispatchers.Main) {
//                        Toast.makeText(this@MainActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//                    Log.e("Socket", "Error: ${e.message}")
//                }
//            }
//        }


//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        socketManager.disconnect()
//    }
//
//    override fun onConnected() {
//        val intent = Intent(this, LoginActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//
//    override fun onConnectionFailed(errorMessage: String) {
//        handler.post {
//            Toast.makeText(this, "Connection failed: $errorMessage", Toast.LENGTH_SHORT).show()
//        }
    }

//    private val connectionReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            btnConnect.text = "Connected"
//            btnConnect.isEnabled = false
//        }
//    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe
    fun onConnectionEvent(event: ConnectionEvent) {
        if (event.connected) {
            btnConnect.text = "Connected"
            btnConnect.isEnabled = false
        }else{
            Toast.makeText(this, "Failed to connect to PLC", Toast.LENGTH_SHORT).show()
        }
    }

}