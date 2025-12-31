package top.kmiit.debughelper.ui.components

import android.hardware.Sensor
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.kmiit.debughelper.R
import top.kmiit.debughelper.ui.viewmodel.SensorViewModel
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SensorTestComponent(
    scrollBehavior: ScrollBehavior,
    onTestClick: (Sensor) -> Unit,
    sensorVM: SensorViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn {
                item {
                    Text(
                        text = stringResource(R.string.sensors),
                        style = MiuixTheme.textStyles.title1,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, start = 16.dp)
                    )
                }

                items(sensorVM.sensors) { sensor ->
                    HorizontalDivider()
                    sensor.Show(onTestClick)
                }
            }
        }
    }
}
