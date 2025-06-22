package clem.project.buzz.ui.components

import android.app.Activity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import clem.project.buzz.ui.theme.BWWhite
import clem.project.buzz.utils.toUtf8
import clem.project.buzz.viewmodels.QuizViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Quiz(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier,
    categoryId: Int
) {
    val activity: Activity = LocalActivity.current!!

    val question  by viewModel.questionState.collectAsState()
    val choices   by viewModel.choicesState .collectAsState()
    val correct   by viewModel.answerState  .collectAsState()
    val error     by viewModel.errorState   .collectAsState()
    val isLoading by viewModel.isLoadingState.collectAsState()

    var questionCount by remember { mutableIntStateOf(1) }
    var selected      by remember { mutableStateOf<String?>(null) }
    var score         by remember { mutableIntStateOf(0) }
    var isNextEnabled by remember { mutableStateOf(false) }
    var secondsLeft   by remember { mutableIntStateOf(0) }
    var showDialog    by remember { mutableStateOf(false) }

    var replaySeconds by remember { mutableIntStateOf(5) }

    LaunchedEffect(questionCount) {
        viewModel.getQuestionByCategory(categoryId)
        selected      = null
        isNextEnabled = false
    }

    LaunchedEffect(selected) {
        if (selected != null) {
            if (selected != correct) {
                showDialog = true
            }
            secondsLeft = 5
            repeat(5) {
                delay(1_000L)
                secondsLeft--
            }
            isNextEnabled = true
        }
    }

    LaunchedEffect(showDialog) {
        if (showDialog) {
            replaySeconds = 5
            repeat(5) {
                delay(1_000L)
                replaySeconds--
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Buzz", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { activity.finish() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor             = BWWhite,
                    navigationIconContentColor = Color.Black,
                    titleContentColor          = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Box(
            Modifier
                .fillMaxSize()
                .background(BWWhite)
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Column(
                modifier            = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.Start
            ) {
                if (isLoading) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                    return@Column
                }
                if (error != null) {
                    Text(error!!, color = Color.Red)
                    return@Column
                }
                if (question == null || correct == null || choices == null) return@Column

                Text("Score : $score", style = MaterialTheme.typography.bodyMedium, color = Color.Black)
                Spacer(Modifier.height(16.dp))
                Text(
                    question!!.question.toUtf8(),
                    style    = MaterialTheme.typography.bodyLarge,
                    color    = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                choices!!.forEach { choice ->
                    val isCorrect = choice == correct
                    val bgColor = when {
                        selected == null                      -> BWWhite
                        isCorrect                              -> Color(0xFF4CAF50)
                        choice == selected && !isCorrect       -> Color(0xFFF44336)
                        else                                   -> BWWhite
                    }
                    BaseButton(
                        text            = choice.toUtf8(),
                        modifier        = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .alpha(if (selected == null || isCorrect || choice == selected) 1f else 0.5f),
                        enabled         = selected == null,
                        backgroundColor = bgColor,
                        contentColor    = if (bgColor != BWWhite) Color.White else Color.Black,
                        onClick         = {
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
                        text            = if (isNextEnabled) "Suivant" else "Patientez ${secondsLeft}s",
                        modifier        = Modifier.fillMaxWidth(),
                        enabled         = isNextEnabled,
                        backgroundColor = BWWhite,
                        contentColor    = Color.Black,
                        onClick         = { questionCount++ }
                    )
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {},
                    title            = { Text("Tu as perdu !", color = Color.Black) },
                    text             = { Text("Ton score : $score", color = Color.Black) },
                    confirmButton    = {
                        TextButton(
                            onClick = {
                                showDialog    = false
                                score          = 0
                                questionCount++
                            },
                            enabled = replaySeconds == 0
                        ) {
                            Text(
                                if (replaySeconds == 0) "Rejouer"
                                else "Rejouer (${replaySeconds}s)"
                            )
                        }
                    },
                    dismissButton    = {
                        TextButton(onClick = { activity.finish() }) {
                            Text("Menu principal")
                        }
                    }
                )
            }
        }
    }
}
