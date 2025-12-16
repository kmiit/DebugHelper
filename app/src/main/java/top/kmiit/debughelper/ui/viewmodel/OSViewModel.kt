package top.kmiit.debughelper.ui.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import top.kmiit.debughelper.utils.PropFactory
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class OSViewModel : ViewModel() {
    val androidVersion: String = Build.VERSION.RELEASE
    val sdkLevel: String = Build.VERSION.SDK_INT.toString()
    val securityPatch: String = Build.VERSION.SECURITY_PATCH
    val vendorSpl: String = PropFactory.get("ro.vendor.build.security_patch", "")
    val fingerprint: String = Build.FINGERPRINT
    val buildTime: String = Instant.ofEpochMilli(Build.TIME).atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"))
    val activeSlot: String = PropFactory.get("ro.boot.slot_suffix", "")
    val kernelVersion: String = System.getProperty("os.version") ?: ""
}
