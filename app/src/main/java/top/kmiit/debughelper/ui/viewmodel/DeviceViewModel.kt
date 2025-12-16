package top.kmiit.debughelper.ui.viewmodel

import android.os.Build
import androidx.lifecycle.ViewModel
import top.kmiit.debughelper.utils.PropFactory

class DeviceViewModel : ViewModel() {
    val manufacturer: String = Build.MANUFACTURER
    val brand: String = Build.BRAND
    val model: String = Build.MODEL
    val product: String = Build.PRODUCT
    val device: String = Build.DEVICE
    val prjname: String = PropFactory.get("ro.boot.prjname", "")
}
