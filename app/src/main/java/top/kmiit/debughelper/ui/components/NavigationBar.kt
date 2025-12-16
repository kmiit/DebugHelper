package top.kmiit.debughelper.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import top.kmiit.debughelper.R
import top.yukonga.miuix.kmp.basic.NavigationBar
import top.yukonga.miuix.kmp.basic.NavigationItem
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.icons.basic.Check
import top.yukonga.miuix.kmp.icon.icons.useful.Info
import top.yukonga.miuix.kmp.icon.icons.useful.Order

@Composable
fun NavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {

    val pages = listOf(
        stringResource(R.string.info),
        stringResource(R.string.test),
        stringResource(R.string.about))
    val items = listOf(
        NavigationItem(stringResource(R.string.info), MiuixIcons.Useful.Order),
        NavigationItem(stringResource(R.string.test), MiuixIcons.Basic.Check),
        NavigationItem(stringResource(R.string.about), MiuixIcons.Useful.Info)
    )
    return NavigationBar(
        items = items,
        selected = selectedIndex,
        onClick = { newIndex -> onItemSelected(newIndex) },
    )
}