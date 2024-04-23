package com.ua.historicalsitesapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.ua.historicalsitesapp.ui.theme.Typography
import com.ua.historicalsitesapp.viewmodels.MainPageViewModel
import com.ua.historicalsitesapp.viewmodels.SelectionState
import kotlinx.coroutines.launch

@Composable
fun CategoriesRow(mainPageViewModel: MainPageViewModel) {
  val coroutineScope = rememberCoroutineScope()
  var showSettings by remember { mutableStateOf(false) }

  // Get the current state of categories
  val categories = mainPageViewModel.categoriesState
  val categoriesToDisplay = categories.value.keys.take(5) + "More"

  Box(
      modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
      contentAlignment = Alignment.TopCenter) {
        LazyRow(
            modifier = Modifier.padding(horizontal = 16.dp).background(Color.Transparent),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              items(categoriesToDisplay) { category ->
                OutlinedButton(
                    onClick = {
                      val newState =
                          when (categories.value[category]) {
                            SelectionState.NOT_SELECTED -> SelectionState.INCLUSIVE
                            SelectionState.INCLUSIVE -> SelectionState.EXCLUSIVE
                            SelectionState.EXCLUSIVE -> SelectionState.NOT_SELECTED
                            else -> SelectionState.NOT_SELECTED
                          }
                      mainPageViewModel.updateCategoryState(category, newState)
                      showSettings = true
                    },
                    modifier = Modifier.padding(4.dp),
                    border =
                        BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                when (categories.value[category]) {
                                  SelectionState.INCLUSIVE -> Color(0xFF90EE90) // Light green
                                  SelectionState.EXCLUSIVE -> Color(0xFFFFCDD2) // Light red
                                  else -> Color.White // Default white color for NOT_SELECTED
                                },
                            contentColor =
                                when (categories.value[category]) {
                                  SelectionState.NOT_SELECTED ->
                                      MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                                  else ->
                                      Color.White // White text color for INCLUSIVE and EXCLUSIVE
                                })) {
                      Text(text = category, style = Typography.labelLarge)
                    }
              }
            }
      }

  SettingsSideSheet(
      show = showSettings,
      onDismiss = { showSettings = false },
      mainPageViewModel = mainPageViewModel)
}

@Composable
fun SettingsSideSheet(show: Boolean, onDismiss: () -> Unit, mainPageViewModel: MainPageViewModel) {
  val screenWidth = LocalConfiguration.current.screenWidthDp.dp
  val slideIn = remember { Animatable(screenWidth.value) }
  val coroutineScope = rememberCoroutineScope()

  LaunchedEffect(show) {
    coroutineScope.launch {
      slideIn.animateTo(
          targetValue = if (show) 0f else screenWidth.value,
          animationSpec = tween(durationMillis = 300))
    }
  }

  AnimatedVisibility(
      visible = show,
      enter = slideInHorizontally { -screenWidth.value.toInt() },
      exit = slideOutHorizontally { screenWidth.value.toInt() },
      modifier = Modifier.fillMaxSize()) {
        Box(
            modifier =
                Modifier.background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize(fraction = 0.8f)
                    .offset { IntOffset(slideIn.value.toInt(), 0) }) {
              SettingsContent(onDismiss, mainPageViewModel)
            }
      }
}

@Composable
fun SettingsContent(onDismiss: () -> Unit, mainPageViewModel: MainPageViewModel) {
  val categories = mainPageViewModel.categoriesState.value

  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Text("Settings", style = MaterialTheme.typography.headlineMedium)
      IconButton(onClick = onDismiss) {
        Icon(imageVector = Icons.Default.Close, contentDescription = "Close Settings")
      }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text("Categories", style = MaterialTheme.typography.titleMedium)
    Spacer(modifier = Modifier.height(8.dp))
    FlowRow(modifier = Modifier.fillMaxWidth(), mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp) {
      categories.forEach { (category, state) ->
        FilterChip(
            onClick = {
              val newState =
                  when (state) {
                    SelectionState.NOT_SELECTED -> SelectionState.INCLUSIVE
                    SelectionState.INCLUSIVE -> SelectionState.EXCLUSIVE
                    SelectionState.EXCLUSIVE -> SelectionState.NOT_SELECTED
                  }
              mainPageViewModel.updateCategoryState(category, newState)
            },
            label = { Text(category) },
            selected = state != SelectionState.NOT_SELECTED,
            colors =
                when (state) {
                  SelectionState.INCLUSIVE ->
                      FilterChipDefaults.filterChipColors(
                          selectedContainerColor = Color(0xFF90EE90))
                  SelectionState.EXCLUSIVE ->
                      FilterChipDefaults.filterChipColors(
                          selectedContainerColor = Color(0xFFFFCDD2))
                  else -> FilterChipDefaults.filterChipColors() // Default colors
                })
      }
    }
    // Add more settings items here if needed
  }
}
