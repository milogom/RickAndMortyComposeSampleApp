package com.example.rickandmorty.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.Result
import com.example.rickandmorty.domain.use_case.GetCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    var state by mutableStateOf(HomeState(isLoading = true))
        private set

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var currentPage = 1

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: String get() = _searchQuery.value

    init {
        _searchQuery
            .debounce(300) // Debounce search input to avoid making too many API calls.
            .onEach { query ->
                if (query.isNotBlank()) {
                    searchCharacters(query)
                } else {
                    getCharacters(increase = false) // Load the initial characters when the search query is empty.
                }
            }
            .launchIn(viewModelScope)
    }

    fun getCharacters(increase: Boolean) {
        viewModelScope.launch {
            if (increase) currentPage++ else if (currentPage > 1) currentPage--
            val showPrevious = currentPage > 1
            val showNext = currentPage < 42
            getCharactersUseCase(currentPage).onEach { result ->
                when (result) {
                    is Result.Success -> {
                        state = state.copy(
                            characters = result.data ?: emptyList(),
                            isLoading = false,
                            showPrevious = showPrevious,
                            showNext = showNext
                        )
                    }
                    is Result.Error -> {
                        state = state.copy(
                            isLoading = false
                        )
                        _eventFlow.emit(
                            UIEvent.ShowSnackBar(
                                result.message ?: "Unknown error"
                            )
                        )
                    }
                    is Result.Loading -> {
                        state = state.copy(
                            isLoading = true
                        )
                    }
                }
            }.launchIn(this)
        }
    }

    sealed class UIEvent {
        data class ShowSnackBar(val message: String) : UIEvent()
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private suspend fun searchCharacters(query: String) {
        if (query.isBlank()) {
            // If the query is empty, load the initial characters.
            getCharacters(increase = false)
            return
        }

        // Update the state to indicate that loading is in progress.
        state = state.copy(isLoading = true)

        // Use the getCharactersUseCase with the search query.
        getCharactersUseCase(currentPage, query).onEach { result ->
            when (result) {
                is Result.Success -> {
                    state = state.copy(
                        characters = result.data ?: emptyList(),
                        isLoading = false,
                        showPrevious = false, // Hide pagination controls when displaying search results.
                        showNext = false
                    )
                }
                is Result.Error -> {
                    state = state.copy(
                        isLoading = false
                    )
                    _eventFlow.emit(
                        UIEvent.ShowSnackBar(
                            result.message ?: "Unknown error"
                        )
                    )
                }
                is Result.Loading -> {
                    state = state.copy(
                        isLoading = true
                    )
                }
            }
        }.launchIn(viewModelScope)
    }

}