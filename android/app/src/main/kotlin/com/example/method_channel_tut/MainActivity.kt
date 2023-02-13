package com.example.method_channel_tut

import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.os.Looper
import android.os.Handler
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.app.AppOpsManager


class MainActivity : FlutterActivity() {
    //! Here [BATTERY_CHANNEL] should be same as you diclared Method channel in dart code
    private val BATTERY_CHANNEL = "dev-devrash/battery"
    private lateinit var channel: MethodChannel
    override fun configureFlutterEngine(@NonNull FlutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine!!)
        channel = MethodChannel(flutterEngine!!.dartExecutor.binaryMessenger, BATTERY_CHANNEL)
        // Receive data from flutter
    //! Here [getBatteryLevel] method name should be same as you use in [invokeMethod] in dart code
        channel.setMethodCallHandler { call, result -> if (call.method == "getBatteryLevel") {
            val args = call.arguments() as Map<String,String>?
            val name = args!!["name"]
            val batterLevel = getBatteryLevel()
            result.success(batterLevel)
        } }
    }
    override fun onCreate(savedInstanceState : Bundle?){
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            val batterLevel = getBatteryLevel()
            channel.invokeMethod("reportBatteryLevel", batterLevel)
        },1000)
    }
    private fun getBatteryLevel() : Int {
        val batteryLevel : Int
        if(VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP){
         val batteryManager = getSystemService(Context.BATTERY_SERVICE) as BatteryManager
         batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        }else{
        val intent = ContextWrapper(applicationContext).registerReceiver(null,IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        batteryLevel = intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100
        }
        return batteryLevel;
    }
}

