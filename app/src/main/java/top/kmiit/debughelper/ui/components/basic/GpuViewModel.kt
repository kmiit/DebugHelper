package top.kmiit.debughelper.ui.components.basic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import top.kmiit.debughelper.utils.GpuInfo
import top.kmiit.debughelper.utils.GpuUtils


class GpuViewModel(application: Application) : AndroidViewModel(application) {
    private val _gpuInfo = MutableStateFlow(GpuInfo())
    val gpuInfo: StateFlow<GpuInfo> = _gpuInfo.asStateFlow()

    init {
        fetchGpuInfo()
    }

    private fun fetchGpuInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val info = GpuUtils.getGpuInfo()
            _gpuInfo.value = info
        }
    }
}
