package top.kmiit.debughelper.ui.viewmodel

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import top.kmiit.debughelper.R
import top.kmiit.debughelper.utils.PropFactory
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class OSViewModel(application: Application) : AndroidViewModel(application) {
    private val unknown = application.getString(R.string.unknown)
    val androidVersion: String = Build.VERSION.RELEASE
    val sdkLevel: String = Build.VERSION.SDK_INT.toString()
    val securityPatch: String = Build.VERSION.SECURITY_PATCH
    val vendorSpl: String = PropFactory.get("ro.vendor.build.security_patch", unknown)
    val fingerprint: String = Build.FINGERPRINT
    val buildTime: String = Instant.ofEpochMilli(Build.TIME).atZone(ZoneId.of("UTC")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'"))
    val activeSlot: String = PropFactory.get("ro.boot.slot_suffix", unknown)
    val kernelVersion: String = System.getProperty("os.version") ?: unknown
}
