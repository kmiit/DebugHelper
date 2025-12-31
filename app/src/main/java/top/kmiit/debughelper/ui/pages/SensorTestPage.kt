package top.kmiit.debughelper.ui.pages

import android.hardware.Sensor
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import top.kmiit.debughelper.R
import top.kmiit.debughelper.ui.components.Batch
import top.kmiit.debughelper.ui.components.ListDetails
import top.kmiit.debughelper.ui.components.TopAppBarSecondary
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.Scaffold

@Composable
fun SensorTestPage(
    sensor: Sensor,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    Scaffold(
        topBar = {
            TopAppBarSecondary(stringResource(R.string.sensor_test_title), onBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                insideMargin = PaddingValues(16.dp)
            ) {
               sensor.Batch()
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxSize(),
                insideMargin = PaddingValues(16.dp)
            ) {
                sensor.ListDetails()
            }
        }
    }
}
