package top.kmiit.debughelper.ui.viewmodel

import android.app.Application
import android.hardware.Sensor
import androidx.lifecycle.AndroidViewModel
import top.kmiit.debughelper.ui.components.batchable
import top.kmiit.debughelper.utils.SensorUtils

class SensorViewModel(application: Application) : AndroidViewModel(application) {
    val sensors: List<Sensor> = SensorUtils.getAllSensors(application).sortedByDescending { it.batchable() }
}
