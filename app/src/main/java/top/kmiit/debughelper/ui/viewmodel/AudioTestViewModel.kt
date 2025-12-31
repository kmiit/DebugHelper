package top.kmiit.debughelper.ui.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioDeviceInfo
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.AudioTrack
import android.media.MediaRecorder
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

enum class AudioChannel {
    LEFT, RIGHT, BOTH
}

class AudioTestViewModel(application: Application) : AndroidViewModel(application) {

    private val audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val _outputDevices = MutableStateFlow<List<AudioDeviceInfo>>(emptyList())
    val outputDevices = _outputDevices.asStateFlow()

    private val _inputDevices = MutableStateFlow<List<AudioDeviceInfo>>(emptyList())
    val inputDevices = _inputDevices.asStateFlow()

    private val _playingDeviceId = MutableStateFlow<Int?>(null)
    val playingDeviceId = _playingDeviceId.asStateFlow()

    private val _recordingDeviceId = MutableStateFlow<Int?>(null)
    val recordingDeviceId = _recordingDeviceId.asStateFlow()
    
    private val _recordingAmplitude = MutableStateFlow(0)
    val recordingAmplitude = _recordingAmplitude.asStateFlow()

    private var audioTrack: AudioTrack? = null
    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null

    init {
        loadDevices()
    }

    fun loadDevices() {
        val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
        _outputDevices.value = devices.toList()

        val inputs = audioManager.getDevices(AudioManager.GET_DEVICES_INPUTS)
        _inputDevices.value = inputs.toList()
    }

    fun playTone(deviceInfo: AudioDeviceInfo, channel: AudioChannel = AudioChannel.BOTH) {
        if (_playingDeviceId.value != null) return

        viewModelScope.launch(Dispatchers.IO) {
            _playingDeviceId.value = deviceInfo.id
            val sampleRate = 44100
            val duration = 3 // seconds
            val numSamples = duration * sampleRate
            val freqOfTone = 440.0 // Hz

            // Stereo: 2 channels * numSamples
            val generatedSnd = ByteArray(2 * 2 * numSamples) 
            val sample = DoubleArray(numSamples)

            // fill out the array
            for (i in 0 until numSamples) {
                sample[i] = sin(2.0 * Math.PI * i.toDouble() / (sampleRate / freqOfTone))
            }

            var idx = 0
            for (dVal in sample) {
                val valShort = (dVal * 32767).toInt().toShort()
                val lowByte = (valShort.toInt() and 0x00ff).toByte()
                val highByte = (valShort.toInt() and 0xff00 ushr 8).toByte()
                
                // Left Channel
                if (channel == AudioChannel.LEFT || channel == AudioChannel.BOTH) {
                     generatedSnd[idx++] = lowByte
                     generatedSnd[idx++] = highByte
                } else {
                     generatedSnd[idx++] = 0
                     generatedSnd[idx++] = 0
                }

                // Right Channel
                if (channel == AudioChannel.RIGHT || channel == AudioChannel.BOTH) {
                     generatedSnd[idx++] = lowByte
                     generatedSnd[idx++] = highByte
                } else {
                     generatedSnd[idx++] = 0
                     generatedSnd[idx++] = 0
                }
            }
            
            val bufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_16BIT
            )

             audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            try {
                audioTrack?.preferredDevice = deviceInfo
                audioTrack?.play()
                audioTrack?.write(generatedSnd, 0, generatedSnd.size)
                
                delay(duration * 1000L)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                 stopPlaying()
            }
        }
    }

    fun stopPlaying() {
        try {
            audioTrack?.stop()
            audioTrack?.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        audioTrack = null
        _playingDeviceId.value = null
    }

    @SuppressLint("MissingPermission")
    fun startRecording(deviceInfo: AudioDeviceInfo) {
         if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        
        if (_recordingDeviceId.value != null) return

        viewModelScope.launch(Dispatchers.IO) {
            _recordingDeviceId.value = deviceInfo.id
            val sampleRate = 44100
            val channelConfig = AudioFormat.CHANNEL_IN_MONO
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT
            val bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat)

            audioRecord = AudioRecord.Builder()
                .setAudioSource(MediaRecorder.AudioSource.MIC)
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(audioFormat)
                        .setSampleRate(sampleRate)
                        .setChannelMask(channelConfig)
                        .build()
                )
                .setBufferSizeInBytes(bufferSize)
                .build()

            try {
                audioRecord?.preferredDevice = deviceInfo
                audioRecord?.startRecording()

                val buffer = ShortArray(bufferSize)
                
                recordingJob = launch {
                    while (isActive && _recordingDeviceId.value != null) {
                        val read = audioRecord?.read(buffer, 0, bufferSize) ?: 0
                        if (read > 0) {
                            // Calculate amplitude
                            var sum = 0L
                            for (i in 0 until read) {
                                sum += Math.abs(buffer[i].toInt())
                            }
                            _recordingAmplitude.value = (sum / read).toInt()
                        }
                    }
                }
                recordingJob?.join()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                stopRecording()
            }
        }
    }

    fun stopRecording() {
        _recordingDeviceId.value = null
        recordingJob?.cancel()
        try {
            if (audioRecord?.recordingState == AudioRecord.RECORDSTATE_RECORDING) {
                audioRecord?.stop()
            }
            audioRecord?.release()
        } catch (e: Exception) {
             e.printStackTrace()
        }
        audioRecord = null
        _recordingAmplitude.value = 0
    }
    
    override fun onCleared() {
        super.onCleared()
        stopPlaying()
        stopRecording()
    }
}
