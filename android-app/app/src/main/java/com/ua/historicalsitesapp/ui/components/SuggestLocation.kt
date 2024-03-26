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

@Composable
fun SuggestLocationForm(onSubmitSuggestion: (String, String) -> Unit, onDismiss: () -> Unit) {
  var title by remember { mutableStateOf("") }
  var shortDescription by remember { mutableStateOf("") }

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
        }
      },
      confirmButton = {
        Button(
            onClick = {
              onSubmitSuggestion(title, shortDescription)
              onDismiss()
            }) {
              Text("Submit")
            }
      },
      dismissButton = { OutlinedButton(onClick = { onDismiss() }) { Text("Cancel") } })
}