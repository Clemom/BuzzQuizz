package clem.project.buzz.ui.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import clem.project.buzz.ui.components.Home
import clem.project.buzz.ui.theme.BuzzQuizzTheme
import clem.project.buzz.viewmodels.HomeViewModel

class HomeActivity : ComponentActivity() {
    private val viewModel: HomeViewModel = HomeViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getCategories()
        enableEdgeToEdge()
        setContent {
            BuzzQuizzTheme {
                Scaffold { innerPadding ->
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(20.dp)
                    ) {
                        Home(viewModel)
                    }
                }
            }
        }
    }
}