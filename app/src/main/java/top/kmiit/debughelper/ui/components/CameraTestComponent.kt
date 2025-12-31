package top.kmiit.debughelper.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import top.kmiit.debughelper.ui.viewmodel.CameraInfoItem
import top.kmiit.debughelper.ui.viewmodel.CameraTestViewModel
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme
import kotlin.coroutines.resume

@OptIn(androidx.camera.camera2.interop.ExperimentalCamera2Interop::class)
@Composable
fun CameraTestComponent(
    scrollBehavior: ScrollBehavior,
    viewModel: CameraTestViewModel = viewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scrollState = rememberScrollState()

    val cameras by viewModel.cameras.collectAsState()
    val selectedCameraId by viewModel.selectedCameraId.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.loadCameras()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            viewModel.loadCameras()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(all = 8.dp)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        if (selectedCameraId != null) {
            Text(
                text = "Preview (Camera ID: $selectedCameraId)",
                style = MiuixTheme.textStyles.title2,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f) 
                ) {
                    val previewView = remember { PreviewView(context) }
                    
                    AndroidView(
                        factory = { 
                            previewView.apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                                scaleType = PreviewView.ScaleType.FILL_CENTER
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )

                    LaunchedEffect(selectedCameraId) {
                        val cameraProvider = context.getCameraProvider()
                        
                        val preview = Preview.Builder().build()
                        preview.setSurfaceProvider(previewView.surfaceProvider)

                        val cameraSelector = CameraSelector.Builder()
                            .addCameraFilter { cameraInfos ->
                                cameraInfos.filter { cameraInfo ->
                                    try {
                                        val id = androidx.camera.camera2.interop.Camera2CameraInfo.from(cameraInfo).cameraId
                                        id == selectedCameraId
                                    } catch (e: Exception) {
                                        false
                                    }
                                }
                            }
                            .build()

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview
                            )
                        } catch (e: Exception) {
                            Log.e("CameraTest", "Use case binding failed", e)
                        }
                    }
                }
                 Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = { viewModel.selectCamera(null) }
                ) {
                     Text(text = "Close Preview")
                }
            }
             Spacer(modifier = Modifier.height(12.dp))
        }

        Text(
            text = "Available Cameras",
            style = MiuixTheme.textStyles.title2,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )

        cameras.forEach { camera ->
            CameraItem(
                camera = camera,
                isSelected = selectedCameraId == camera.id,
                onSelect = {
                     viewModel.selectCamera(camera.id)
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun CameraItem(
    camera: CameraInfoItem,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ID: ${camera.id} (${camera.facing})",
                style = MiuixTheme.textStyles.subtitle
            )
            Text(
                text = "Resolution: ${camera.resolution}",
                style = MiuixTheme.textStyles.body2
            )
             Text(
                text = "Orientation: ${camera.sensorOrientation}°",
                style = MiuixTheme.textStyles.body2
            )
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSelect,
                enabled = !isSelected
            ) {
                 Text(text = if (isSelected) "Previewing" else "Preview")
            }
        }
    }
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider =
    suspendCancellableCoroutine { continuation ->
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            continuation.resume(cameraProviderFuture.get())
        }, ContextCompat.getMainExecutor(this))
    }
