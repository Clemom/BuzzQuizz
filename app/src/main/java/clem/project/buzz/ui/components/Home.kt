package clem.project.buzz.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import clem.project.buzz.models.api.Category
import clem.project.buzz.ui.views.QuizActivity
import clem.project.buzz.utils.switchActivity
import clem.project.buzz.viewmodels.HomeViewModel
import clem.project.buzz.ui.theme.BWWhite
import androidx.compose.material.icons.filled.EmojiEvents
import clem.project.buzz.ui.views.LeaderboardActivity


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(viewModel: HomeViewModel) {
    val categories by viewModel.categoriesState.collectAsState()
    var selected by remember { mutableStateOf<Category?>(null) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buzz", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Filled.Settings, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {
                        switchActivity(context, LeaderboardActivity::class.java)
                    }) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = "Leaderboard")
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor             = BWWhite,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor     = Color.Black,
                    titleContentColor          = Color.Black
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BWWhite)
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))

            Card(
                modifier  = Modifier.fillMaxWidth(),
                shape     = RoundedCornerShape(12.dp),
                elevation = CardDefaults.elevatedCardElevation(4.dp)
            ) {
                Column(
                    modifier           = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BaseDropdown(
                        label          = "Choisissez une catégorie",
                        options        = categories ?: emptyList(),
                        selectedOption = selected,
                        onOptionSelect = { selected = it },
                        toString       = { it.name }
                    )
                    Spacer(Modifier.height(16.dp))
                    BaseButton(
                        text    = "BUZZ MOI ÇA !",
                        enabled = (selected != null),
                        onClick = {
                            selected?.let {
                                switchActivity(context, QuizActivity::class.java) {
                                    putExtra("categoryId", it.id)
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}