package clem.project.buzz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import clem.project.buzz.utils.toUtf8
import clem.project.buzz.viewmodels.QuizViewModel

@Composable
fun Quiz(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier,
    categoryId: Int
) {
    val question   by viewModel.questionState   .collectAsState()
    val choices    by viewModel.choicesState    .collectAsState()
    val correct    by viewModel.answerState     .collectAsState()
    val error      by viewModel.errorState      .collectAsState()
    val isLoading  by viewModel.isLoadingState  .collectAsState()
    val canFetch   by viewModel.canFetchState   .collectAsState()

    var questionCount by remember { mutableIntStateOf(1) }
    var selected      by remember { mutableStateOf<String?>(null) }
    var score         by remember { mutableIntStateOf(0) }

    LaunchedEffect(questionCount) {
        viewModel.getQuestionByCategory(categoryId)
        selected = null
    }

    Column(
        verticalArrangement   = Arrangement.Center,
        horizontalAlignment   = Alignment.CenterHorizontally,
        modifier              = modifier.padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator()
            return@Column
        }
        if (error != null) {
            Text(text = error!!, color = Color.Red)
            return@Column
        }
        if (question == null || correct == null || choices == null) return@Column

        // En-tête
        Text("FizzQuiz", style = MaterialTheme.typography.headlineMedium)
        Text("Score : $score", modifier = Modifier.padding(vertical = 8.dp))
        Spacer(Modifier.height(24.dp))

        // Question
        Text(
            text     = question!!.question.toUtf8(),
            style    = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        choices!!.forEach { choice ->
            val isCorrect  = choice == correct
            val isSelected = choice == selected

            val alpha = when {
                selected == null        -> 1f
                isCorrect || isSelected -> 1f
                else                     -> 0.5f
            }

            val containerColor = when {
                selected == null -> Color.White
                isCorrect        -> Color(0xFF4CAF50)
                isSelected       -> Color(0xFFF44336)
                else              -> Color.White
            }

            BaseButton(
                text           = choice.toUtf8(),
                modifier       = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .graphicsLayer { this.alpha = alpha },
                enabled        = (selected == null),
                gradientColors = listOf(containerColor, containerColor),
                textColor      = Color.Black,
                cornerRadius   = 4.dp,
                elevation      = 2.dp,
                verticalPadding= 12.dp,
                onClick        = {
                    if (selected == null) {
                        selected = choice
                        viewModel.checkAnswer(choice)
                        if (choice == correct) score++
                    }
                }
            )
        }

        if (selected != null) {
            Spacer(Modifier.height(24.dp))
            BaseButton(
                text           = "Suivant",
                modifier       = Modifier.fillMaxWidth(),
                enabled        = canFetch,
                gradientColors = listOf(Color.White, Color.White),
                textColor      = Color.Black,
                cornerRadius   = 4.dp,
                elevation      = 2.dp,
                verticalPadding= 12.dp,
                onClick        = { questionCount++ }
            )
        }
    }
}
