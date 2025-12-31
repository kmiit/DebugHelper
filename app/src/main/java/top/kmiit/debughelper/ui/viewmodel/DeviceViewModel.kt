package top.kmiit.debughelper.ui.viewmodel

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import top.kmiit.debughelper.R
import top.kmiit.debughelper.utils.PropFactory

class DeviceViewModel(application: Application) : AndroidViewModel(application){

    private val unknown = application.getString(R.string.unknown)
    val manufacturer: String = Build.MANUFACTURER
    val brand: String = Build.BRAND
    val model: String = Build.MODEL
    val product: String = Build.PRODUCT
    val device: String = Build.DEVICE
    val prjname: String = PropFactory.get("ro.boot.prjname", "")
    val unlocked: String = PropFactory.get("ro.boot.vbmeta.device_state", unknown)
}
