package top.kmiit.debughelper.ui.components.gnss

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import top.kmiit.debughelper.R
import top.yukonga.miuix.kmp.basic.Button
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.theme.MiuixTheme

@Composable
fun GNSSTestComponent(
    scrollBehavior: ScrollBehavior,
    vm: GNSSTestViewModel = viewModel()
) {
    val satellites by vm.satellites.collectAsState()
    val context = LocalContext.current
    var isQuerying = false
    
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true || 
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            vm.querySatellites()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 8.dp)
            .nestedScroll(scrollBehavior.nestedScrollConnection)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
        ) {
            LazyColumn {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.gnss),
                            style = MiuixTheme.textStyles.title1,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { 
                                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                        vm.querySatellites()
                                    } else {
                                        permissionLauncher.launch(arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        ))
                                    }
                                    isQuerying = true
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(if (isQuerying) satellites.size.toString() else stringResource(R.string.start))
                            }
                            Button(
                                onClick = {
                                    vm.stopQuery()
                                    isQuerying = false
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(stringResource(R.string.stop))
                            }
                        }
                    }
                }

                item {
                    HorizontalDivider()
                }

                if (satellites.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No satellites found",
                                color = MiuixTheme.colorScheme.onSurface
                            )
                        }
                    }
                } else {
                    items(satellites) { satellite ->
                        SatelliteItem(satellite)
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
fun SatelliteItem(satellite: SatelliteInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "ID: ${satellite.svid}",
                style = MiuixTheme.textStyles.subtitle,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (satellite.usedInFix) "Used" else "In View",
                color = if (satellite.usedInFix) MiuixTheme.colorScheme.primary else MiuixTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
             Text(
                 text = "Constellation: ${satellite.constellationName}",
                 style = MiuixTheme.textStyles.body2
             )
             Text(
                 text = "SNR: ${satellite.cn0DbHz}",
                 style = MiuixTheme.textStyles.body2
             )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(
             modifier = Modifier.fillMaxWidth(),
             horizontalArrangement = Arrangement.SpaceBetween
        ) {
             Text(
                 text = "Az: ${satellite.azimuthDegrees}°",
                 style = MiuixTheme.textStyles.body2
             )
             Text(
                 text = "El: ${satellite.elevationDegrees}°",
                 style = MiuixTheme.textStyles.body2
             )
        }
    }
}
