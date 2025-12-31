package top.kmiit.debughelper.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import top.kmiit.debughelper.R
import top.yukonga.miuix.kmp.basic.NavigationBar
import top.yukonga.miuix.kmp.basic.NavigationItem
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.basic.Check
import top.yukonga.miuix.kmp.icon.extended.Info
import top.yukonga.miuix.kmp.icon.extended.Album

@Composable
fun NavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        NavigationItem(stringResource(R.string.info), MiuixIcons.Album),
        NavigationItem(stringResource(R.string.test), MiuixIcons.Basic.Check),
        NavigationItem(stringResource(R.string.about), MiuixIcons.Info)
    )
    return NavigationBar(
        items = items,
        selected = selectedIndex,
        onClick = { newIndex -> onItemSelected(newIndex) },
    )
}