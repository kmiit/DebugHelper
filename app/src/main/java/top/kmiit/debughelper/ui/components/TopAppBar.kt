package top.kmiit.debughelper.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import top.kmiit.debughelper.R
import top.yukonga.miuix.kmp.basic.Icon
import top.yukonga.miuix.kmp.basic.IconButton
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.TopAppBar
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.extended.Back

@Composable
fun TopAppBar(scrollBehavior: ScrollBehavior) {
    return TopAppBar(
        title = stringResource(R.string.app_name),
        scrollBehavior = scrollBehavior,
    )
}

@Composable
fun TopAppBarSecondary(title: String, action: () -> Unit) {
    return TopAppBar(
        title = title,
        navigationIcon = {
            IconButton(onClick = action) {
                Icon(
                    imageVector = MiuixIcons.Back,
                    contentDescription = "Back"
                )
            }
        }
    )
}