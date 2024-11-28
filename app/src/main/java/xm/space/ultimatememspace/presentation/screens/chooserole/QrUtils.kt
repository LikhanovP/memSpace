package xm.space.ultimatememspace.presentation.screens.chooserole

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter

fun getFormattedAddress(context: Context, port: Int): String {
    return "${getIp(context = context)}&$port"
}

private fun getIp(context: Context): String {
    return Formatter.formatIpAddress(
        (context.getSystemService(Context.WIFI_SERVICE) as WifiManager).connectionInfo.ipAddress
    )
}