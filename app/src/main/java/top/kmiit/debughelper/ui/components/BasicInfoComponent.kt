package top.kmiit.debughelper.ui.components

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.kmiit.debughelper.R
import top.kmiit.debughelper.ui.viewmodel.DeviceViewModel
import top.kmiit.debughelper.ui.viewmodel.HardwareViewModel
import top.kmiit.debughelper.ui.viewmodel.OSViewModel
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun BasicInfoComponent(
    scrollBehavior: ScrollBehavior, 
    deviceVM: DeviceViewModel = viewModel(),
    osVM: OSViewModel = viewModel(),
    hwVM: HardwareViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(all = 8.dp)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            insideMargin = PaddingValues(16.dp)
        ) {
            Text(
                text = stringResource(R.string.device),
                style = MiuixTheme.textStyles.title1,
                fontWeight = FontWeight.Bold
            )
            ShowItem(stringResource(R.string.manufacturer), deviceVM.manufacturer)
            ShowItem(stringResource(R.string.brand), deviceVM.brand)
            ShowItem(stringResource(R.string.model), deviceVM.model)
            ShowItem(stringResource(R.string.product), deviceVM.product)
            ShowItem(stringResource(R.string.device_l), deviceVM.device)
            deviceVM.prjname.takeIf { it.isNotEmpty() }?.let {
                ShowItem(stringResource(R.string.prjname), it)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            insideMargin = PaddingValues(16.dp)
        ) {
            Text(
                text = stringResource(R.string.os),
                style = MiuixTheme.textStyles.title1,
                fontWeight = FontWeight.Bold
            )
            ShowItem(stringResource(R.string.android_version), osVM.androidVersion)
            ShowItem(stringResource(R.string.sdk_level), osVM.sdkLevel)
            ShowItem(stringResource(R.string.security_patch), osVM.securityPatch)
            ShowItem(stringResource(R.string.vendor_spl), osVM.vendorSpl)
            ShowItem(stringResource(R.string.fingerprint), osVM.fingerprint)
            ShowItem(stringResource(R.string.build_time), osVM.buildTime)
            ShowItem(stringResource(R.string.active_slot), osVM.activeSlot)
            ShowItem(stringResource(R.string.kernel_version), osVM.kernelVersion)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            insideMargin = PaddingValues(16.dp)
        ) {
            Text(
                text = stringResource(R.string.hardware),
                style = MiuixTheme.textStyles.title1,
                fontWeight = FontWeight.Bold
            )
//            ShowItem(stringResource(R.string.android_version), hwVM.androidVersion)
        }
    }
}


@Composable
private fun ShowItem(key: String, value: String) {
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = key,
            modifier = Modifier.weight(1f),
            style = MiuixTheme.textStyles.subtitle
        )
        Text(value, modifier = Modifier.weight(1f))
    }
}
