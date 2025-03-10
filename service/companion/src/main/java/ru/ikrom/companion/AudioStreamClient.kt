package ru.ikrom.companion

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.io.OutputStream
import java.net.Socket
import java.nio.ByteBuffer
import java.nio.ByteOrder

class AudioStreamClient {
    private var socket: Socket? = null
    private var outputStream: OutputStream? = null

    suspend fun connect(serverIp: String, serverPort: Int) = withContext(Dispatchers.IO) {
        runCatching {
            socket = Socket(serverIp, serverPort)
            outputStream = socket?.getOutputStream()
            Log.i(TAG, "Connected to $serverIp:$serverPort")
        }.onFailure { e ->
            Log.e(TAG, "Connection error: ${e.message}")
        }
    }

    suspend fun sendData(data: ByteArray) = withContext(Dispatchers.IO){
        socket ?: return@withContext
        val output = DataOutputStream(socket!!.getOutputStream())
        val header = ByteBuffer.allocate(4)
            .order(ByteOrder.BIG_ENDIAN)
            .putInt(data.size)
            .array()

        output.write(header)
        output.write(data)
        output.flush()
    }

    fun disconnect() {
        runCatching {
            outputStream?.close()
            socket?.close()
            Log.i(TAG, "Disconnected")
        }.onFailure { e ->
            Log.e(TAG, "Disconnection error: ${e.message}")
        }
    }

    companion object {
        private val TAG = AudioStreamClient::class.simpleName
    }
}