package com.example.socketconnectiontest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.net.Socket

class LoginActivity : AppCompatActivity() {

    private lateinit var txtUsername: EditText
    private lateinit var txtPassword: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val ipAddress = intent.getStringExtra("IP_ADDRESS") ?: ""
        val port = intent.getIntExtra("PORT", 0)

        txtUsername = findViewById(R.id.txtUsername)
        txtPassword = findViewById(R.id.txtPass)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val username = txtUsername.text.toString()
            val password = txtPassword.text.toString()

            var isConnected = false

            GlobalScope.launch(Dispatchers.IO) {
                isConnected = sendLoginCommand(ipAddress, port, username, password)
                if (isConnected) {
                    startRectangleModeActivity(ipAddress, port, username, password)
                } else {
                    runOnUiThread {
                        // Handle connection failure (e.g., show error message)
                    }
                }
            }
        }

    }

    private fun sendLoginCommand(
        ipAddress: String,
        port: Int,
        username: String,
        password: String
    ): Boolean {
        var isConnected = false
        val socket = Socket(ipAddress, port)

        try {
            val writer = BufferedWriter(OutputStreamWriter(socket.getOutputStream()))
            val command =
                "<PLC_VICON><INITIALIZE SEQUENCE=\"nnn\"><USER_NAME>$username</USER_NAME><PASSWORD>$password</PASSWORD></INITIALIZE></PLC_VICON>"
            writer.write(command)
            writer.flush()

            isConnected = true

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            socket.close()
        }

        return isConnected
    }

    private fun startRectangleModeActivity(ipAddress: String, port: Int, username: String, password: String) {
        val intent = Intent(this@LoginActivity, ChangeLayoutActivity::class.java).apply {
            putExtra("IP_ADDRESS", ipAddress)
            putExtra("PORT", port)
        }
        startActivity(intent)
        finish() // Finish LoginActivity to prevent going back to it using back button
    }


}