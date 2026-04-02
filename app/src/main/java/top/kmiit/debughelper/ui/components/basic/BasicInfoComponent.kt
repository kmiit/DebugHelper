package top.kmiit.debughelper.ui.components.basic

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import top.kmiit.debughelper.R
import top.kmiit.debughelper.ui.components.Utils.ShowItem
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun BasicInfoComponent(
    scrollBehavior: ScrollBehavior,
    deviceVM: DeviceViewModel = viewModel(),
    osVM: OSViewModel = viewModel(),
) {
    val scrollState = rememberScrollState()
    val deviceInfo by deviceVM.deviceInfo.collectAsState()
    val osInfo by osVM.osInfo.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(8.dp)
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
            ShowItem(stringResource(R.string.manufacturer), deviceInfo.manufacturer)
            ShowItem(stringResource(R.string.brand), deviceInfo.brand)
            ShowItem(stringResource(R.string.model), deviceInfo.model)
            ShowItem(stringResource(R.string.product), deviceInfo.product)
            ShowItem(stringResource(R.string.device_l), deviceInfo.device)
            deviceInfo.prjname.takeIf { it.isNotEmpty() }?.let {
                ShowItem(stringResource(R.string.prjname), it)
            }
            ShowItem(stringResource(R.string.unlocked), deviceInfo.unlocked)
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
            ShowItem(stringResource(R.string.android_version), osInfo.androidVersion)
            ShowItem(stringResource(R.string.sdk_level), osInfo.sdkLevel)
            ShowItem(stringResource(R.string.security_patch), osInfo.securityPatch)
            ShowItem(stringResource(R.string.vendor_spl), osInfo.vendorSpl)
            ShowItem(stringResource(R.string.fingerprint), osInfo.fingerprint)
            ShowItem(stringResource(R.string.build_time), osInfo.buildTime)
            ShowItem(stringResource(R.string.active_slot), osInfo.activeSlot)
            ShowItem(stringResource(R.string.kernel_version), osInfo.kernelVersion)
        }
    }
}