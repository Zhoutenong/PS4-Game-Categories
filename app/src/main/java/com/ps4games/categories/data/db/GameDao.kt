package com.ps4games.categories.data.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class CategoryCount(val primary_category: String, val count: Int)
data class Stats(val totalGames: Int, val totalCategories: Int)

@Dao
interface GameDao {

    @Query("SELECT primary_category, COUNT(*) as count FROM games GROUP BY primary_category ORDER BY count DESC")
    fun getCategories(): Flow<List<CategoryCount>>

    @Query("SELECT * FROM games WHERE primary_category = :category ORDER BY id")
    fun getGamesByCategory(category: String): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE name LIKE '%' || :query || '%' OR name_en LIKE '%' || :query || '%' OR tags LIKE '%' || :query || '%'")
    fun searchGames(query: String): Flow<List<GameEntity>>

    @Query("SELECT * FROM games WHERE id = :id")
    fun getGameById(id: Int): Flow<GameEntity?>

    @Query("SELECT COUNT(*) as totalGames, COUNT(DISTINCT primary_category) as totalCategories FROM games")
    fun getStats(): Flow<Stats>
}
