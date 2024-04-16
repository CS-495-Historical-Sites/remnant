package com.ua.historicalsitesapp.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.compose.HistoricalSitesAppTheme
import com.google.accompanist.flowlayout.FlowRow
import com.ua.historicalsitesapp.viewmodels.UserProfileViewModel

// Asks the user questions to gain more information about their historical preference
class QuestionnaireActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      HistoricalSitesAppTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          val context = LocalContext.current
          val userProfilePageModel = UserProfileViewModel(context)
          QuestionnaireMenu(
              onQuestionnaireCompleted = userProfilePageModel::submitRegistrationQuestionnaire)
        }
      }
    }
  }
}

@Composable
fun QuestionnaireMenu(onQuestionnaireCompleted: (Map<String, Set<String>>) -> Unit) {
    val context = LocalContext.current
    var currentQuestionIndex by remember { mutableIntStateOf(0) }

    val questions =
        listOf(
            "Preferred Historical Period:" to
                    listOf(
                        "Ancient Civilization",
                        "Middle Ages",
                        "Renaissance",
                        "Industrial Revolution",
                        "Modern Era"),
            "Preferred Historical Genre in Literature:" to
                    listOf(
                        "Historical Fiction",
                        "Biography",
                        "Non-Fiction History"),
            "What type of historical sites are you most interested in exploring?" to
                    listOf(
                        "Ancient Landmarks and Ruins",
                        "Cities with Rich Historical Architecture",
                        "Natural Sites with Historical Significance",
                        "Museums and Exhibits",
                        "Diverse Cultural Heritage Areas"))

    val allAnswers = remember { mutableStateMapOf<String, Set<String>>() }
    val selectedOptionsMap = remember { mutableStateMapOf<Int, Set<String>>() }

    QuestionnaireCard(
        currentQuestionIndex = currentQuestionIndex,
        question = questions[currentQuestionIndex].first,
        options = questions[currentQuestionIndex].second,
        selectedOptions = selectedOptionsMap[currentQuestionIndex] ?: emptySet(),
        onAnswer = { options, question ->
            selectedOptionsMap[currentQuestionIndex] = options.toSet()
            allAnswers[question] = options.toSet()
        },
        onNext = {
            if (currentQuestionIndex + 1 >= questions.size) {
                // When questionnaire is completed, pass all answers to the callback
                onQuestionnaireCompleted(allAnswers)
                val intent = Intent(context, MainPageActivity::class.java)
                context.startActivity(intent)
            } else {
                currentQuestionIndex++
            }
        }
    )
}


// Sets up the chip ui
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionnaireCard(
    currentQuestionIndex: Int,
    question: String,
    options: List<String>,
    selectedOptions: Set<String>,
    onAnswer: (Set<String>, String) -> Unit,
    onNext: () -> Unit
) {
  Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
    Text(text = question, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))

    FlowRow(
        mainAxisSpacing = 8.dp,
        crossAxisSpacing = 8.dp,
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)) {
          options.forEach { option ->
            FilterChip(
                onClick = {
                  val newOptions =
                      if (selectedOptions.contains(option)) {
                        selectedOptions - option
                      } else {
                        selectedOptions + option
                      }
                  onAnswer(newOptions, question)
                },
                label = { Text(option) },
                selected = selectedOptions.contains(option),
                leadingIcon =
                    if (selectedOptions.contains(option)) {
                      {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize))
                      }
                    } else {
                      null
                    },
            )
          }
        }

      HorizontalDivider(
          modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), thickness = 1.dp,
          color = Color.Gray.copy(alpha = 0.4f)
      )

    Button(onClick = { onNext() }, modifier = Modifier.fillMaxWidth()) {
      val questionsSize = 3
      Text(text = if (currentQuestionIndex < questionsSize - 1) "Next" else "Finish")
    }
  }
}
