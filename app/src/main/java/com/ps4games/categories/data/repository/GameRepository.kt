package com.ps4games.categories.data.repository

import com.ps4games.categories.data.db.CategoryCount
import com.ps4games.categories.data.db.GameDao
import com.ps4games.categories.data.db.GameEntity
import com.ps4games.categories.data.db.Stats
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(private val dao: GameDao) {

    fun getCategories(): Flow<List<CategoryCount>> = dao.getCategories()

    fun getGamesByCategory(category: String): Flow<List<GameEntity>> =
        dao.getGamesByCategory(category)

    fun searchGames(query: String): Flow<List<GameEntity>> = dao.searchGames(query)

    fun getGameById(id: Int): Flow<GameEntity?> = dao.getGameById(id)

    fun getStats(): Flow<Stats> = dao.getStats()
}
