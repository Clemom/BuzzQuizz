package clem.project.buzz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import clem.project.buzz.models.api.Category
import clem.project.buzz.ui.views.QuizActivity
import clem.project.buzz.utils.switchActivity
import clem.project.buzz.viewmodels.HomeViewModel

@Composable
fun Home(viewModel: HomeViewModel) {
    var selected by remember { mutableStateOf<Category?>(null) }
    val context = LocalContext.current
    val categories by viewModel.categoriesState.collectAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Buzz",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(24.dp))

        BaseDropdown<Category>(
            label = "Choose a category",
            options = categories ?: emptyList(),
            selectedOption = selected,
            onOptionSelect = { selected = it },
            toString = { it.name }
        )

        Spacer(Modifier.height(24.dp))

        BaseButton(
            text = "BUZZ MOI ÇA !",
            modifier = Modifier.fillMaxWidth(),
            enabled = (selected != null),
            gradientColors = listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.secondary
            ),
            textColor = MaterialTheme.colorScheme.onPrimary,
            onClick = {
                selected?.let { category ->
                    switchActivity(context, QuizActivity::class.java) {
                        putExtra("categoryId", category.id)
                    }
                }
            }
        )
    }
}
