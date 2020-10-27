package com.example.locationsample

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Binder
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat

private const val CHANNEL_ID = "notifChannel"
private const val NOTIF_ID = 987
private const val TAG = "LocationSample"

class LocationService : Service(), LocationListener {

    private val locationManager by lazy { applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    override fun onCreate() {
        Log.e(TAG, "onCreate service")
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_NONE)
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("My Location sample")
                .setContentText("Updating location...").build()

        Log.e(TAG, "onCreate service - startForeground")
        startForeground(NOTIF_ID, notification)
        startLocationUpdates()
    }

    override fun onDestroy() {
        removeLocationUpdates()
        super.onDestroy()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        }
    }

    private fun removeLocationUpdates() {
        locationManager.removeUpdates(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return Binder("binder")
    }

    override fun onLocationChanged(location: Location) {
        Log.e(TAG, "onLocation changed by ${location.provider} location $location")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.e(TAG, "onProviderDisabled $provider")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.e(TAG, "onProviderEnabled $provider")
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.e(TAG, "onStatusChanged $status")
    }

}