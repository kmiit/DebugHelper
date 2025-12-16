package top.kmiit.debughelper.ui.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import top.kmiit.debughelper.R
import top.kmiit.debughelper.ui.components.BasicInfoComponent
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.utils.overScrollVertical
import top.yukonga.miuix.kmp.basic.TabRowWithContour

@Composable
fun InfoPage(paddingValues: PaddingValues, scrollBehavior: ScrollBehavior) {
    val tabs = listOf(stringResource(R.string.basic_info), "Following", "Popular", "Featured")
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    Column(modifier = Modifier
        .fillMaxSize()
        .overScrollVertical()
        .nestedScroll(scrollBehavior.nestedScrollConnection)
        .padding(
            top = paddingValues.calculateTopPadding(),
            bottom = paddingValues.calculateBottomPadding()
        ),
    ) {
        TabRowWithContour(
            modifier = Modifier,
            tabs = tabs,
            selectedTabIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
        )
        when(selectedTabIndex){
            0 -> BasicInfoComponent(scrollBehavior)
            else -> Text("test")
        }
    }
}