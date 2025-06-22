package clem.project.buzz.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LeaderboardViewModelFactory(
    private val ctx: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass == LeaderboardViewModel::class.java)
        return LeaderboardViewModel(ctx) as T
    }
}
