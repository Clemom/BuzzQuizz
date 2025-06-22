package clem.project.buzz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import clem.project.buzz.data.RetrofitInstance
import clem.project.buzz.models.api.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val categoryApi = RetrofitInstance.quizApi

    private val _categoriesState = MutableStateFlow<List<Category>?>(null)
    val categoriesState : StateFlow<List<Category>?> = _categoriesState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    fun getCategories() {
        viewModelScope.launch {
            try {
                val response = categoryApi.getCategories()

                val categories = response?.trivia_categories

                if(categories != null) _categoriesState.value = categories
                else _errorState.value = "No categories found"

            } catch (e: Exception) {
                print(e)
                _errorState.value = "Error: ${e.message}"
            }
        }
    }
}