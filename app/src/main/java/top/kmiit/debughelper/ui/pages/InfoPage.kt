package top.kmiit.debughelper.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import top.kmiit.debughelper.R
import top.kmiit.debughelper.ui.components.BasicInfoComponent
import top.kmiit.debughelper.ui.components.DisplayInfoComponent
import top.kmiit.debughelper.ui.components.SocInfoComponent
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.TabRow
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.utils.overScrollVertical

@Composable
fun InfoPage(paddingValues: PaddingValues, scrollBehavior: ScrollBehavior) {
    val tabs = listOf(
        stringResource(R.string.basic_info),
        stringResource(R.string.hardware_info),
        stringResource(R.string.display_info),
        stringResource(R.string.sensors)
    )
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .overScrollVertical()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .padding(
                top = paddingValues.calculateTopPadding(),
                bottom = paddingValues.calculateBottomPadding()
            ),
    ) {
        TabRow(
            tabs = tabs,
            selectedTabIndex = pagerState.currentPage,
            onTabSelected = { index ->
                coroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            },
            minWidth = 140.dp,
            modifier = Modifier.padding(8.dp)
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) {
            when (it) {
                0 -> BasicInfoComponent(scrollBehavior)
                1 -> SocInfoComponent(scrollBehavior)
                2 -> DisplayInfoComponent(scrollBehavior)
                else -> Text("test")
            }
        }
    }
}
