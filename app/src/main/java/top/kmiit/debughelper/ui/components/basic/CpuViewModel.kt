package top.kmiit.debughelper.ui.components.basic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import top.kmiit.debughelper.R
import top.kmiit.debughelper.utils.NodeFactory
import top.kmiit.debughelper.utils.PropFactory
import java.io.File
import java.util.regex.Pattern

data class CpuInfo(
    val soc: String = "",
    val abiList: String = "",
    val governor: String = "",
    val cpuCores: Int = 0
)

class CpuViewModel(application: Application) : AndroidViewModel(application) {
    private val _cpuInfo = MutableStateFlow(CpuInfo())
    val cpuInfo: StateFlow<CpuInfo> = _cpuInfo.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val applicationContext = getApplication<Application>()
            val unknown = applicationContext.getString(R.string.unknown)
            _cpuInfo.value = CpuInfo(
                soc = PropFactory.get("ro.soc.model", unknown),
                abiList = PropFactory.get("ro.system.product.cpu.abilist", unknown),
                governor = NodeFactory.readSysNode(
                    applicationContext,
                    "/sys/devices/system/cpu/cpu0/cpufreq/scaling_governor"
                ),
                cpuCores = try {
                    File("/sys/devices/system/cpu/").listFiles { _, name ->
                        Pattern.matches("cpu[0-9]+", name)
                    }?.size ?: 0
                } catch (e: Exception) {
                    0
                }
            )
        }
    }
}