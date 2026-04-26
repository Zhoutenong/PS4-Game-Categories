package com.ps4games.categories.ui.home

import androidx.lifecycle.ViewModel
import com.ps4games.categories.data.db.CategoryCount
import com.ps4games.categories.data.db.Stats
import com.ps4games.categories.data.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(repo: GameRepository) : ViewModel() {
    val categories: Flow<List<CategoryCount>> = repo.getCategories()
    val stats: Flow<Stats> = repo.getStats()
}
