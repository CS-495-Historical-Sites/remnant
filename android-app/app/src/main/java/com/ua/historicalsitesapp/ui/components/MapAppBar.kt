package com.ua.historicalsitesapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapAppBar(scope: CoroutineScope, drawerState: DrawerState) {
  CenterAlignedTopAppBar(
      title = {},
      navigationIcon = {
        IconButton(
            onClick = {
              scope.launch { drawerState.apply { if (isClosed) open() else close() } }
            }) {
              Icon(imageVector = Icons.Filled.Menu, contentDescription = "Open menu")
            }
      },
      actions = {},
      colors =
          TopAppBarDefaults.topAppBarColors(
              containerColor = Color(0xfffce4ec),
              titleContentColor = Color.DarkGray,
              navigationIconContentColor = Color.DarkGray,
              actionIconContentColor = MaterialTheme.colorScheme.onSecondary))
}
