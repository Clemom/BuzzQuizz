package clem.project.buzz.ui.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import clem.project.buzz.ui.components.LeaderboardScreen
import clem.project.buzz.viewmodels.LeaderboardViewModel
import clem.project.buzz.viewmodels.LeaderboardViewModelFactory

class LeaderboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val factory = remember { LeaderboardViewModelFactory(this) }
            val vm: LeaderboardViewModel = viewModel(factory = factory)
            LeaderboardScreen(vm)
        }
    }
}
