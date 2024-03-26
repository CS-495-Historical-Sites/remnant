package com.ua.historicalsitesapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.ua.historicalsitesapp.data.model.map.HsLocationComplete

@Composable
fun SuggestEditForm(
    location: HsLocationComplete,
    onSubmitSuggestion: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
  var title by remember { mutableStateOf(location.name) }
  var shortDescription by remember { mutableStateOf(location.shortDescription ?: "") }
  var longDescription by remember { mutableStateOf(location.longDescription ?: "") }

  AlertDialog(
      onDismissRequest = { onDismiss() },
      title = { Text("Suggest an Edit") },
      text = {
        Column {
          TextField(value = title, onValueChange = { title = it }, label = { Text("Name") })
          TextField(
              value = shortDescription,
              onValueChange = { shortDescription = it },
              label = { Text("Short Description") })
          TextField(
              value = longDescription,
              onValueChange = { longDescription = it },
              label = { Text("Long Description") })
        }
      },
      confirmButton = {
        Button(
            onClick = {
              onSubmitSuggestion(title, shortDescription, longDescription)
              onDismiss()
            }) {
              Text("Submit")
            }
      },
      dismissButton = { OutlinedButton(onClick = { onDismiss() }) { Text("Cancel") } })
}
