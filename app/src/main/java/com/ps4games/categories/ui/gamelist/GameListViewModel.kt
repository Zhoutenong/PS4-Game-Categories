package com.ps4games.categories.ui.gamelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ps4games.categories.data.db.GameEntity
import com.ps4games.categories.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

enum class SortOrder { DEFAULT, SCORE_DESC, SCORE_ASC }

@HiltViewModel
class GameListViewModel @Inject constructor(private val repo: GameRepository) : ViewModel() {

    private val _category = MutableStateFlow("")
    private val _sortOrder = MutableStateFlow(SortOrder.DEFAULT)

    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    val games: StateFlow<List<GameEntity>> = combine(_category, _sortOrder) { cat, sort ->
        Pair(cat, sort)
    }.flatMapLatest { (cat, sort) ->
        if (cat.isEmpty()) flowOf(emptyList())
        else repo.getGamesByCategory(cat).map { list ->
            when (sort) {
                SortOrder.SCORE_DESC -> list.sortedByDescending { it.score }
                SortOrder.SCORE_ASC -> list.sortedBy { it.score }
                SortOrder.DEFAULT -> list
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setCategory(category: String) { _category.value = category }
    fun setSortOrder(order: SortOrder) { _sortOrder.value = order }
}
