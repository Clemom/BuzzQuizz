package clem.project.buzz.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import clem.project.buzz.ui.theme.BWWhite
import clem.project.buzz.utils.toUtf8
import clem.project.buzz.viewmodels.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Quiz(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier,
    categoryId: Int
) {
    val question   by viewModel.questionState.collectAsState()
    val choices    by viewModel.choicesState.collectAsState()
    val correct    by viewModel.answerState.collectAsState()
    val error      by viewModel.errorState.collectAsState()
    val isLoading  by viewModel.isLoadingState.collectAsState()
    val canFetch   by viewModel.canFetchState.collectAsState()

    var questionCount by remember { mutableIntStateOf(1) }
    var selected      by remember { mutableStateOf<String?>(null) }
    var score         by remember { mutableIntStateOf(0) }

    LaunchedEffect(questionCount) {
        viewModel.getQuestionByCategory(categoryId)
        selected = null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buzz", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { /* back */ }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = BWWhite,
                    navigationIconContentColor = Color.Black,
                    titleContentColor       = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BWWhite)
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // État chargement
            if (isLoading) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.Black)
                }
                return@Column
            }

            // État erreur
            if (error != null) {
                Text(text = error!!, color = Color.Black)
                return@Column
            }

            // Données non prêtes
            if (question == null || correct == null || choices == null) return@Column

            // Affiche score et question
            Text(
                text  = "Score : $score",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text     = question!!.question.toUtf8(),
                style    = MaterialTheme.typography.bodyLarge,
                color    = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Liste de choix
            choices!!.forEach { choice ->
                val isCorrect  = choice == correct
                val isSelected = choice == selected
                val alpha      = if (selected == null || isCorrect || isSelected) 1f else 0.5f

                BaseButton(
                    text    = choice.toUtf8(),
                    modifier= Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .alpha(alpha),
                    enabled = (selected == null),
                    onClick = {
                        if (selected == null) {
                            selected = choice
                            viewModel.checkAnswer(choice)
                            if (choice == correct) score++
                        }
                    }
                )
            }

            // Bouton "Suivant"
            if (selected != null) {
                Spacer(Modifier.height(24.dp))
                BaseButton(
                    text    = "Suivant",
                    modifier= Modifier
                        .fillMaxWidth()
                        .alpha(if (canFetch) 1f else 0.5f),
                    enabled = canFetch,
                    onClick = { questionCount++ }
                )
            }
        }
    }
}
