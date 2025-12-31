package top.kmiit.debughelper.ui.components.gnss

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.GnssStatus
import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationRequest
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import top.kmiit.debughelper.R

data class SatelliteInfo(
    val svid: Int,
    val constellationType: Int,
    val constellationName: String,
    val cn0DbHz: Float,
    val elevationDegrees: Float,
    val azimuthDegrees: Float,
    val usedInFix: Boolean
)

class GNSSTestViewModel(application: Application) : AndroidViewModel(application) {
    private val _satellites = MutableStateFlow<List<SatelliteInfo>>(emptyList())
    val satellites = _satellites.asStateFlow()

    private val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var isListening = false
    private var lastUpdateTime = 0L
    
    private val gnssStatusCallback = object : GnssStatus.Callback() {
        override fun onSatelliteStatusChanged(status: GnssStatus) {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastUpdateTime < 1000) {
                return
            }
            lastUpdateTime = currentTime

            val satelliteList = mutableListOf<SatelliteInfo>()
            val count = status.satelliteCount
            for (i in 0 until count) {
                val type = status.getConstellationType(i)
                satelliteList.add(
                    SatelliteInfo(
                        svid = status.getSvid(i),
                        constellationType = type,
                        constellationName = getConstellationName(type),
                        cn0DbHz = status.getCn0DbHz(i),
                        elevationDegrees = status.getElevationDegrees(i),
                        azimuthDegrees = status.getAzimuthDegrees(i),
                        usedInFix = status.usedInFix(i)
                    )
                )
            }
            _satellites.value = satelliteList
        }
    }

    private fun getConstellationName(id: Int): String {
        val context = getApplication<Application>()
        return when (id) {
            GnssStatus.CONSTELLATION_GPS -> context.getString(R.string.constellation_gps)
            GnssStatus.CONSTELLATION_SBAS -> context.getString(R.string.constellation_sbas)
            GnssStatus.CONSTELLATION_GLONASS -> context.getString(R.string.constellation_glonass)
            GnssStatus.CONSTELLATION_QZSS -> context.getString(R.string.constellation_qzss)
            GnssStatus.CONSTELLATION_BEIDOU -> context.getString(R.string.constellation_beidou)
            GnssStatus.CONSTELLATION_GALILEO -> context.getString(R.string.constellation_galileo)
            GnssStatus.CONSTELLATION_IRNSS -> context.getString(R.string.constellation_irnss)
            GnssStatus.CONSTELLATION_UNKNOWN -> context.getString(R.string.unknown)
            else -> context.getString(R.string.unknown)
        }
    }

    private val locationListener = LocationListener {
        // Keep GPS active
    }

    fun querySatellites() {
        if (ActivityCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        
        if (isListening) return

        locationManager.registerGnssStatusCallback(getApplication<Application>().mainExecutor, gnssStatusCallback)
        
        // Request updates as fast as possible to ensure GNSS is active
        // But we throttle the UI updates in the callback
        val locationRequest = LocationRequest.Builder(0L)
            .setMinUpdateIntervalMillis(0L)
            .setQuality(LocationRequest.QUALITY_HIGH_ACCURACY)
            .setMinUpdateDistanceMeters(0f)
            .build()

        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            locationRequest,
            getApplication<Application>().mainExecutor,
            locationListener
        )
        
        isListening = true
    }

    fun stopQuery() {
        if (isListening) {
            locationManager.unregisterGnssStatusCallback(gnssStatusCallback)
            locationManager.removeUpdates(locationListener)
            isListening = false
        }
        _satellites.value = emptyList()
    }

    override fun onCleared() {
        super.onCleared()
        stopQuery()
    }
}
