package top.kmiit.debughelper.ui.components.audio

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import top.kmiit.debughelper.R
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun AudioTestComponent(
    scrollBehavior: ScrollBehavior,
    viewModel: AudioTestViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    val outputDevices by viewModel.outputDevices.collectAsState()
    val inputDevices by viewModel.inputDevices.collectAsState()
    val playingDeviceId by viewModel.playingDeviceId.collectAsState()
    val recordingDeviceId by viewModel.recordingDeviceId.collectAsState()
    val amplitude by viewModel.recordingAmplitude.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.loadDevices()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(all = 8.dp)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Text(
            text = stringResource(R.string.output_devices),
            style = MiuixTheme.textStyles.title2,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        outputDevices.forEach { device ->
            val isSpeaker = device.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER ||
                            device.type == AudioDeviceInfo.TYPE_WIRED_HEADSET ||
                            device.type == AudioDeviceInfo.TYPE_WIRED_HEADPHONES ||
                            device.type == AudioDeviceInfo.TYPE_USB_HEADSET ||
                            device.type == AudioDeviceInfo.TYPE_BLUETOOTH_A2DP
            
            val isPlaying = playingDeviceId == device.id
            val isAnyPlaying = playingDeviceId != null

            AudioOutputDeviceItem(
                device = device,
                isPlaying = isPlaying,
                isAnyPlaying = isAnyPlaying,
                isAnyRecording = recordingDeviceId != null,
                isStereoSupported = isSpeaker,
                onPlay = { channel ->
                    if (isPlaying) viewModel.stopPlaying() else viewModel.playTone(device, channel)
                },
                onStop = { viewModel.stopPlaying() }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(R.string.input_devices),
            style = MiuixTheme.textStyles.title2,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        inputDevices.forEach { device ->
            val isRecording = recordingDeviceId == device.id
            val isAnyRecording = recordingDeviceId != null

            AudioInputDeviceItem(
                device = device,
                isRecording = isRecording,
                isAnyRecording = isAnyRecording,
                isAnyPlaying = playingDeviceId != null,
                amplitude = amplitude,
                onRecord = {
                    if (isRecording) viewModel.stopRecording() else viewModel.startRecording(device)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun AudioOutputDeviceItem(
    device: AudioDeviceInfo,
    isPlaying: Boolean,
    isAnyPlaying: Boolean,
    isAnyRecording: Boolean,
    isStereoSupported: Boolean,
    onPlay: (AudioChannel) -> Unit,
    onStop: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = device.getDeviceName(),
                style = MiuixTheme.textStyles.subtitle
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (isPlaying) {
                 Button(
                    onClick = onStop,
                    enabled = true
                ) {
                     Text(stringResource(R.string.stop))
                }
            } else {
                Row {
                    if (isStereoSupported) {
                        Button(
                            onClick = { onPlay(AudioChannel.LEFT) },
                            enabled = !isAnyRecording && !isAnyPlaying,
                            modifier = Modifier.weight(1f)
                        ) {
                             Text(stringResource(R.string.left))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = { onPlay(AudioChannel.RIGHT) },
                            enabled = !isAnyRecording && !isAnyPlaying,
                            modifier = Modifier.weight(1f)
                        ) {
                             Text(stringResource(R.string.right))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    
                    Button(
                        onClick = { onPlay(AudioChannel.BOTH) },
                        enabled = !isAnyRecording && !isAnyPlaying,
                        modifier = Modifier.weight(1f)
                    ) {
                         Text(if (isStereoSupported)
                                stringResource(R.string.both)
                                else stringResource(R.string.play))
                    }
                }
            }
        }
    }
}

@Composable
fun AudioInputDeviceItem(
    device: AudioDeviceInfo,
    isRecording: Boolean,
    isAnyRecording: Boolean,
    isAnyPlaying: Boolean,
    amplitude: Int,
    onRecord: () -> Unit
) {
     Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = device.getDeviceName(),
                style = MiuixTheme.textStyles.subtitle
            )
            if (isRecording) {
                Text(
                    text = stringResource(R.string.amplitude) + ": $amplitude",
                    style = MiuixTheme.textStyles.body2
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onRecord,
                enabled = !isAnyPlaying && (isRecording || !isAnyRecording)
            ) {
                 Text(if (isRecording)
                        stringResource(R.string.stop)
                     else stringResource(R.string.record))
            }
        }
    }
}

fun AudioDeviceInfo.getDeviceName(): String {
    val typeName = when (type) {
        AudioDeviceInfo.TYPE_BUILTIN_EARPIECE -> "Built-in Earpiece"
        AudioDeviceInfo.TYPE_BUILTIN_SPEAKER -> "Built-in Speaker"
        AudioDeviceInfo.TYPE_WIRED_HEADSET -> "Wired Headset"
        AudioDeviceInfo.TYPE_WIRED_HEADPHONES -> "Wired Headphones"
        AudioDeviceInfo.TYPE_BLUETOOTH_SCO -> "Bluetooth SCO"
        AudioDeviceInfo.TYPE_BLUETOOTH_A2DP -> "Bluetooth A2DP"
        AudioDeviceInfo.TYPE_BUILTIN_MIC -> "Built-in Microphone"
        AudioDeviceInfo.TYPE_FM_TUNER -> "FM Tuner"
        AudioDeviceInfo.TYPE_TV_TUNER -> "TV Tuner"
        AudioDeviceInfo.TYPE_TELEPHONY -> "Telephony"
        AudioDeviceInfo.TYPE_AUX_LINE -> "Aux Line"
        AudioDeviceInfo.TYPE_IP -> "IP"
        AudioDeviceInfo.TYPE_BUS -> "Bus"
        AudioDeviceInfo.TYPE_USB_DEVICE -> "USB Device"
        AudioDeviceInfo.TYPE_USB_HEADSET -> "USB Headset"
        AudioDeviceInfo.TYPE_HEARING_AID -> "Hearing Aid"
        AudioDeviceInfo.TYPE_BLE_HEADSET -> "BLE Headset"
        AudioDeviceInfo.TYPE_BLE_SPEAKER -> "BLE Speaker"
        AudioDeviceInfo.TYPE_BLE_BROADCAST -> "BLE Broadcast"
        AudioDeviceInfo.TYPE_REMOTE_SUBMIX -> "Remote Submit"
//        AudioDeviceInfo.TYPE_ECHO_REFERENCE -> "Echo Reference"
        else -> "Unknown Type ($type)"
    }
    return "$typeName ${if (address.isNotEmpty()) "($address)" else ""}"
}
