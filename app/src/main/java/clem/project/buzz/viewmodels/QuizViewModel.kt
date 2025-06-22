package clem.project.buzz.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import clem.project.buzz.data.RetrofitInstance
import clem.project.buzz.models.api.Question
import clem.project.buzz.utils.ResponseCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuizViewModel: ViewModel() {
    private val quizApi = RetrofitInstance.quizApi

    private val _questionState = MutableStateFlow<Question?>(null)
    val questionState: StateFlow<Question?> = _questionState

    private val _choicesState = MutableStateFlow<List<String>?>(null)
    val choicesState: StateFlow<List<String>?> = _choicesState

    private val _answerState = MutableStateFlow<String?>(null)
    val answerState: StateFlow<String?> = _answerState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    private val _isLoadingState = MutableStateFlow<Boolean>(false)
    val isLoadingState: StateFlow<Boolean> = _isLoadingState

    private val _canFetchState = MutableStateFlow<Boolean>(true)
    val canFetchState: StateFlow<Boolean> = _canFetchState

    fun checkAnswer(answer: String): Boolean = answer == _answerState.value

    fun getQuestion() {
        if (!_canFetchState.value) return

        viewModelScope.launch {
            _canFetchState.value = false
            _isLoadingState.value = true

            try {
                val response = quizApi.getOneQuestion()
                val responseCode = ResponseCode.fromCode(response.responseCode)

                when (responseCode) {
                    ResponseCode.SUCCESS -> {
                        val question = response.results.firstOrNull()
                        val correct = response.results.firstOrNull()?.correct_answer
                        val choices = response.results.firstOrNull()?.incorrect_answers?.toMutableList()
                            ?.apply {
                                response.results.firstOrNull()?.correct_answer?.let { add(it) }
                            }?.shuffled()

                        if (question != null && choices != null && correct != null) {
                            _questionState.value = question
                            _answerState.value = correct
                            _choicesState.value = choices
                            _errorState.value = null
                        } else {
                            _errorState.value = "No question found"
                        }
                    }

                    ResponseCode.NO_RESULTS -> _errorState.value = "No results available."
                    ResponseCode.INVALID_PARAMETER -> _errorState.value = "Invalid parameter."
                    ResponseCode.TOKEN_NOT_FOUND -> _errorState.value = "Token not found."
                    ResponseCode.TOKEN_EMPTY -> _errorState.value = "Token exhausted."
                    ResponseCode.RATE_LIMIT -> _errorState.value = "Rate limit exceeded."
                    ResponseCode.UNKNOWN -> _errorState.value = "Unknown error code: ${response.responseCode}"
                }
            } catch (e: Exception) {
                _errorState.value = "Error: ${e.message}"
            } finally {
                _isLoadingState.value = false

                delay(5_000)
                _canFetchState.value = true
            }
        }
    }

    fun getQuestionByCategory(id: Int) {
        if (!_canFetchState.value) return

        viewModelScope.launch {
            _canFetchState.value = false
            _isLoadingState.value = true

            try {
                val response = quizApi.getOneQuestionByCategory(id)
                val responseCode = ResponseCode.fromCode(response.responseCode)

                when (responseCode) {
                    ResponseCode.SUCCESS -> {
                        val question = response.results.firstOrNull()
                        val correct = response.results.firstOrNull()?.correct_answer
                        val choices = response.results.firstOrNull()?.incorrect_answers?.toMutableList()
                            ?.apply {
                                response.results.firstOrNull()?.correct_answer?.let { add(it) }
                            }?.shuffled()

                        if (question != null && choices != null && correct != null) {
                            _questionState.value = question
                            _answerState.value = correct
                            _choicesState.value = choices
                            _errorState.value = null
                        } else {
                            _errorState.value = "No question found"
                        }
                    }

                    ResponseCode.NO_RESULTS -> _errorState.value = "No results available."
                    ResponseCode.INVALID_PARAMETER -> _errorState.value = "Invalid parameter."
                    ResponseCode.TOKEN_NOT_FOUND -> _errorState.value = "Token not found."
                    ResponseCode.TOKEN_EMPTY -> _errorState.value = "Token exhausted."
                    ResponseCode.RATE_LIMIT -> _errorState.value = "Rate limit exceeded."
                    ResponseCode.UNKNOWN -> _errorState.value = "Unknown error code: ${response.responseCode}"
                }
            } catch (e: Exception) {
                _errorState.value = "Error: ${e.message}"
            } finally {
                _isLoadingState.value = false

                delay(5_000)
                _canFetchState.value = true
            }
        }
    }
}