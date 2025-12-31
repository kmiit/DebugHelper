package top.kmiit.debughelper.ui.viewmodel

import android.app.Application
import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CameraInfoItem(
    val id: String,
    val facing: String,
    val resolution: String,
    val sensorOrientation: Int
)

class CameraTestViewModel(application: Application) : AndroidViewModel(application) {

    private val cameraManager = application.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    private val _cameras = MutableStateFlow<List<CameraInfoItem>>(emptyList())
    val cameras = _cameras.asStateFlow()

    private val _selectedCameraId = MutableStateFlow<String?>(null)
    val selectedCameraId = _selectedCameraId.asStateFlow()

    init {
        loadCameras()
    }

    fun loadCameras() {
        try {
            val cameraIds = cameraManager.cameraIdList
            val cameraList = cameraIds.map { id ->
                val characteristics = cameraManager.getCameraCharacteristics(id)
                val facing = when (characteristics.get(CameraCharacteristics.LENS_FACING)) {
                    CameraCharacteristics.LENS_FACING_FRONT -> "Front"
                    CameraCharacteristics.LENS_FACING_BACK -> "Back"
                    CameraCharacteristics.LENS_FACING_EXTERNAL -> "External"
                    else -> "Unknown"
                }
                
                // Get sensor resolution
                val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                val sizes = map?.getOutputSizes(android.graphics.ImageFormat.JPEG)
                val maxResolution = sizes?.maxByOrNull { it.width * it.height }
                val resolutionStr = if (maxResolution != null) {
                    "${maxResolution.width}x${maxResolution.height} (${(maxResolution.width * maxResolution.height) / 1000000}MP)"
                } else {
                    "Unknown"
                }
                
                val orientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0

                CameraInfoItem(id, facing, resolutionStr, orientation)
            }
            _cameras.value = cameraList
        } catch (e: Exception) {
            e.printStackTrace()
            _cameras.value = emptyList()
        }
    }

    fun selectCamera(id: String?) {
        _selectedCameraId.value = id
    }
}
