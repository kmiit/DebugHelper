package top.kmiit.debughelper.ui.components.basic

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import top.kmiit.debughelper.R
import top.kmiit.debughelper.utils.PropFactory

data class DeviceInfo(
    val manufacturer: String = "",
    val brand: String = "",
    val model: String = "",
    val product: String = "",
    val device: String = "",
    val prjname: String = "",
    val unlocked: String = ""
)

class DeviceViewModel(application: Application) : AndroidViewModel(application){

    private val _deviceInfo = MutableStateFlow(DeviceInfo())
    val deviceInfo: StateFlow<DeviceInfo> = _deviceInfo.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val unknown = getApplication<Application>().getString(R.string.unknown)
            _deviceInfo.value = DeviceInfo(
                manufacturer = Build.MANUFACTURER,
                brand = Build.BRAND,
                model = Build.MODEL,
                product = Build.PRODUCT,
                device = Build.DEVICE,
                prjname = PropFactory.get("ro.boot.prjname", ""),
                unlocked = PropFactory.get("ro.boot.vbmeta.device_state", unknown)
            )
        }
    }
}
