package top.kmiit.debughelper.ui.components

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
import top.kmiit.debughelper.ui.viewmodel.CpuViewModel
import top.kmiit.debughelper.ui.viewmodel.GpuViewModel
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun SocInfoComponent(
    scrollBehavior: ScrollBehavior,
    cpuVM: CpuViewModel = viewModel(),
    gpuVM: GpuViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    val gpuInfo by gpuVM.gpuInfo.collectAsState()

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
                text = stringResource(R.string.cpu),
                style = MiuixTheme.textStyles.title1,
                fontWeight = FontWeight.Bold
            )
            ShowItem(stringResource(R.string.platform), cpuVM.soc)
            ShowItem(stringResource(R.string.abi_list), cpuVM.abiList)
            ShowItem(stringResource(R.string.governor), cpuVM.governor)
            ShowItem(stringResource(R.string.cpu_cores), cpuVM.cpuCores.toString())
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            insideMargin = PaddingValues(16.dp)
        ) {
            Text(
                text = stringResource(R.string.gpu),
                style = MiuixTheme.textStyles.title1,
                fontWeight = FontWeight.Bold
            )
            ShowItem(stringResource(R.string.gpu_vendor), gpuInfo.vendor)
            ShowItem(stringResource(R.string.gpu_model), gpuInfo.renderer)
            ShowItem(stringResource(R.string.opengl), gpuInfo.version)
            ShowItem(stringResource(R.string.vulkan), gpuInfo.vulkanVersion)
        }
    }
}
