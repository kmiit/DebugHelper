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
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class OsInfo(
    val androidVersion: String = "",
    val sdkLevel: String = "",
    val securityPatch: String = "",
    val vendorSpl: String = "",
    val fingerprint: String = "",
    val buildTime: String = "",
    val activeSlot: String = "",
    val kernelVersion: String = ""
)

class OSViewModel(application: Application) : AndroidViewModel(application) {
    private val _osInfo = MutableStateFlow(OsInfo())
    val osInfo: StateFlow<OsInfo> = _osInfo.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val unknown = getApplication<Application>().getString(R.string.unknown)
            _osInfo.value = OsInfo(
                androidVersion = Build.VERSION.RELEASE,
                sdkLevel = Build.VERSION.SDK_INT.toString(),
                securityPatch = Build.VERSION.SECURITY_PATCH,
                vendorSpl = PropFactory.get("ro.vendor.build.security_patch", unknown),
                fingerprint = Build.FINGERPRINT,
                buildTime = Instant.ofEpochMilli(Build.TIME)
                    .atZone(ZoneId.of("UTC"))
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'")),
                activeSlot = PropFactory.get("ro.boot.slot_suffix", unknown),
                kernelVersion = System.getProperty("os.version") ?: unknown
            )
        }
    }
}