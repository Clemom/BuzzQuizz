package clem.project.buzz.ui.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import clem.project.buzz.ui.components.LeaderboardScreen
import clem.project.buzz.ui.theme.BuzzQuizzTheme
import clem.project.buzz.viewmodels.LeaderboardViewModel

class LeaderboardActivity : ComponentActivity() {
    private lateinit var vm: LeaderboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm = LeaderboardViewModel(applicationContext)
        setContent {
            BuzzQuizzTheme {
                LeaderboardScreen(vm)
            }
        }
    }
}
