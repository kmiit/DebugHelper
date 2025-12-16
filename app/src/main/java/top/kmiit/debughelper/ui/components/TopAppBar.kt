package top.kmiit.debughelper.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import top.kmiit.debughelper.R
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.TopAppBar

@Composable
fun TopAppBar(scrollBehavior: ScrollBehavior) {
    return TopAppBar(
        title = stringResource(R.string.app_name),
        scrollBehavior = scrollBehavior,
    )
}
