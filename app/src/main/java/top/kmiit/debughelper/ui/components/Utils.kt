package top.kmiit.debughelper.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

object Utils {
    @Composable
    fun ShowItem(key: String, value: Any) {
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = key,
                modifier = Modifier.weight(1f),
                style = MiuixTheme.textStyles.subtitle
            )
            Text(value.toString(), modifier = Modifier.weight(1f))
        }
    }
}