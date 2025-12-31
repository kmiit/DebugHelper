package top.kmiit.debughelper.ui.components.nfc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
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
fun NfcTestComponent(
    scrollBehavior: ScrollBehavior,
    vm: NfcTestViewModel = viewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column (modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Text(
                    text = stringResource(R.string.nfc),
                    style = MiuixTheme.textStyles.title1,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                ShowItem(stringResource(R.string.is_secure_nfc_supported), vm.isSecureNfcSupported)
                ShowItem(stringResource(R.string.is_secure_nfc_enabled), vm.isSecureNfcEnabled)
                ShowItem(stringResource(R.string.is_nfc_enabled), vm.isEnabled)

                if(vm.isEnabled) {
                    ShowItem(stringResource(R.string.nfc_tag),
                        vm.tagInfo.ifEmpty { stringResource(R.string.waiting) })
                }
            }
        }
    }
}
