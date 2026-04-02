package top.kmiit.debughelper.ui.components

import android.hardware.Sensor
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import top.kmiit.debughelper.R
import top.kmiit.debughelper.ui.pages.AboutPage
import top.kmiit.debughelper.ui.pages.InfoPage
import top.kmiit.debughelper.ui.pages.SensorTestPage
import top.kmiit.debughelper.ui.pages.TestPage
import top.yukonga.miuix.kmp.basic.NavigationBar as MiuixNavigationBar
import top.yukonga.miuix.kmp.basic.NavigationBarItem
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.icon.MiuixIcons
import top.yukonga.miuix.kmp.icon.basic.Check
import top.yukonga.miuix.kmp.icon.extended.Album
import top.yukonga.miuix.kmp.icon.extended.Info

sealed interface AppNavKey : NavKey {
    data object Info : AppNavKey
    data object Test : AppNavKey
    data object About : AppNavKey
    data object SensorTest : AppNavKey
}

@Immutable
private data class NavigationItemConfig(
    val key: AppNavKey,
    val labelResId: Int,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)

private val NAVIGATION_ITEMS = listOf(
    NavigationItemConfig(AppNavKey.Info, R.string.info, MiuixIcons.Album),
    NavigationItemConfig(AppNavKey.Test, R.string.test, MiuixIcons.Basic.Check),
    NavigationItemConfig(AppNavKey.About, R.string.about, MiuixIcons.Info)
)

/**
 * App导航栏组件
 * 仅在不是SensorTest页面时显示
 */
@Composable
fun NavigationBar(
    currentRoute: AppNavKey,
    onNavigate: (AppNavKey) -> Unit
) {
    // 仅在不是SensorTest时显示导航栏
    if (currentRoute == AppNavKey.SensorTest) {
        return
    }

    MiuixNavigationBar {
        NAVIGATION_ITEMS.forEach { config ->
            NavigationBarItem(
                selected = config.key == currentRoute,
                onClick = { onNavigate(config.key) },
                icon = config.icon,
                label = stringResource(config.labelResId)
            )
        }
    }
}

@Composable
fun NavigationController(
    backStack: MutableList<AppNavKey>,
    selectedSensor: Sensor?,
    paddingValues: PaddingValues,
    scrollBehavior: ScrollBehavior,
    onSelectedSensorChange: (Sensor?) -> Unit
) {
    val handleBack = remember(backStack, onSelectedSensorChange) {
        {
            if (backStack.size > 1) {
                if (backStack.last() == AppNavKey.SensorTest) {
                    onSelectedSensorChange(null)
                }
                backStack.removeAt(backStack.lastIndex)
            }
        }
    }
    val onTestSensor = remember(backStack, onSelectedSensorChange) {
        { sensor: Sensor ->
            onSelectedSensorChange(sensor)
            if (backStack.lastOrNull() != AppNavKey.SensorTest) {
                backStack.add(AppNavKey.SensorTest)
            }
        }
    }
    val onSensorTestBack = remember(backStack, onSelectedSensorChange) {
        {
            onSelectedSensorChange(null)
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        }
    }
    val latestPaddingValues = rememberUpdatedState(paddingValues)
    val latestScrollBehavior = rememberUpdatedState(scrollBehavior)
    val latestSelectedSensor = rememberUpdatedState(selectedSensor)
    val latestOnTestSensor = rememberUpdatedState(onTestSensor)
    val latestOnSensorTestBack = rememberUpdatedState(onSensorTestBack)

    val appEntryProvider = remember {
        entryProvider {
            entry<AppNavKey.Info> {
                InfoPage(latestPaddingValues.value, latestScrollBehavior.value)
            }
            entry<AppNavKey.Test> {
                TestPage(
                    latestPaddingValues.value,
                    latestScrollBehavior.value,
                    onTestSensor = latestOnTestSensor.value
                )
            }
            entry<AppNavKey.About> {
                AboutPage(latestPaddingValues.value, latestScrollBehavior.value)
            }
            entry<AppNavKey.SensorTest> {
                val sensor = latestSelectedSensor.value
                if (sensor != null) {
                    SensorTestPage(
                        sensor = sensor,
                        onBack = latestOnSensorTestBack.value
                    )
                }
            }
        }
    }

    NavDisplay(
        backStack = backStack,
        onBack = handleBack,
        entryProvider = appEntryProvider
    )
}
