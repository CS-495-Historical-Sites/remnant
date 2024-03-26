package com.ua.historicalsitesapp.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import com.ua.historicalsitesapp.data.model.map.ClusterItem
import com.ua.historicalsitesapp.viewmodels.MainPageViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationInfoCard(
    mainPageViewModel: MainPageViewModel,
    selectedLocation: ClusterItem,
    onDismissRequest: () -> Unit,
) {
  val sheetState = rememberModalBottomSheetState()
  ModalBottomSheet(
      onDismissRequest = onDismissRequest,
      sheetState = sheetState,
  ) {
    // Sheet content
    val info = selectedLocation.let { mainPageViewModel.getLocationInfo(it.itemId) }
    LocationInfoCardContent(location = info)
  }
}
