package top.kmiit.debughelper.utils

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager

object SensorUtils {
    fun getAllSensors(context: Context): List<Sensor> {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        return sensorManager.getSensorList(Sensor.TYPE_ALL)
    }
}
