package uk.ac.tees.mad.d3840711

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LatestNewsViewModel : ViewModel() {
    private val networkService = NetworkService.apiService

    val data = mutableStateOf<latestNewsResponses?>(null)
    val error = mutableStateOf<String?>(null)

    fun fetchData() {
        viewModelScope.launch {
            try {
                val response = networkService.getLatestNews()
                if (response.isSuccessful) {
                    data.value = response.body()
                } else {
                    error.value = "Error: ${response.code()}"
                }
            } catch (e: Exception) {
                error.value = "Error: ${e.message}"
            }
        }
    }
}