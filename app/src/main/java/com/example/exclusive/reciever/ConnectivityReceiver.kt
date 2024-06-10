package com.example.exclusive.reciever
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.example.exclusive.NoInternetActivity
import com.example.exclusive.internet

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

            if (!isConnected) {
                Log.d("ConnectivityReceiver", "Internet Disconnected")
                Toast.makeText(context, "Internet Disconnected", Toast.LENGTH_SHORT).show()
                internet.postValue(false)
                val intent = Intent(context, NoInternetActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
            else {
                internet.postValue(true)
                Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show()
            }
        } else {
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            val isConnected = activeNetwork?.isConnectedOrConnecting == true

            if (!isConnected) {
                Log.d("ConnectivityReceiver", "Internet Disconnected")
                Toast.makeText(context, "Internet Disconnected", Toast.LENGTH_SHORT).show()
                internet.postValue(false)
                val intent = Intent(context, NoInternetActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
            else {
                internet.postValue(true)
                Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
