package clem.project.buzz.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import clem.project.buzz.data.local.AppDatabase
import clem.project.buzz.data.local.Score
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LeaderboardViewModel(ctx: Context) : ViewModel() {
    private val dao = AppDatabase.get(ctx).scoreDao()

    val topScores: StateFlow<List<Score>> = dao
        .topScores(limit = 10)
        .onEach {}
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun refresh(limit: Int = 10) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.topScores(limit)
                .collect { scores ->
                }
        }
    }
}
