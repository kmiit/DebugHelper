package top.kmiit.debughelper.ui.components.basic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import top.kmiit.debughelper.R
import top.kmiit.debughelper.utils.NodeFactory
import top.kmiit.debughelper.utils.PropFactory
import java.io.File
import java.util.regex.Pattern

class CpuViewModel(application: Application) : AndroidViewModel(application) {
    private val unknown = application.getString(R.string.unknown)
    val soc: String = PropFactory.get("ro.soc.model", unknown)
    val abiList: String = PropFactory.get("ro.system.product.cpu.abilist", unknown)
    val governor: String =
        NodeFactory.readSysNode(application, "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor")
    val cpuCores: Int = try {
        File("/sys/devices/system/cpu/").listFiles { _, name ->
            Pattern.matches("cpu[0-9]+", name)
        }?.size ?: 0
    } catch (e: Exception) {
        0
    }
}