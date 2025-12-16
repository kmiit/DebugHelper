import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import top.kmiit.debughelper.ui.components.NavigationBar
import top.kmiit.debughelper.ui.components.TopAppBar
import top.kmiit.debughelper.ui.pages.AboutPage
import top.kmiit.debughelper.ui.pages.InfoPage
import top.kmiit.debughelper.ui.pages.TestPage
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.theme.ColorSchemeMode
import top.yukonga.miuix.kmp.theme.MiuixTheme
import top.yukonga.miuix.kmp.theme.ThemeController

@Composable
fun App(
) {
    MiuixTheme(
        controller = ThemeController(ColorSchemeMode.System),
    ) {
        val scrollBehavior = MiuixScrollBehavior()
        var selectedIndex by remember { mutableIntStateOf(0) }

        Scaffold(
            topBar = { TopAppBar(scrollBehavior) },
            bottomBar = { NavigationBar(selectedIndex) { newIndex -> selectedIndex = newIndex } }
        ) { paddingValues ->
            when (selectedIndex) {
                0 -> InfoPage(paddingValues, scrollBehavior)
                1 -> TestPage(paddingValues, scrollBehavior)
                2 -> AboutPage(paddingValues, scrollBehavior)
            }
        }
    }
}
