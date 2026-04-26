package com.ps4games.categories.data.repository

import com.ps4games.categories.data.db.CategoryCount
import com.ps4games.categories.data.db.GameDao
import com.ps4games.categories.data.db.GameEntity
import com.ps4games.categories.data.db.Stats
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GameRepositoryTest {

    private lateinit var dao: GameDao
    private lateinit var repo: GameRepository

    private val game1 = GameEntity(1, "ゲーム1", "Game1", 85, listOf("Action"), "Action")
    private val game2 = GameEntity(2, "ゲーム2", "Game2", 70, listOf("RPG"), "RPG")

    @Before
    fun setup() {
        dao = mockk()
        repo = GameRepository(dao)
    }

    @Test
    fun `getCategories delegates to dao`() = runTest {
        val expected = listOf(CategoryCount("Action", 5), CategoryCount("RPG", 3))
        every { dao.getCategories() } returns flowOf(expected)

        val result = repo.getCategories().first()

        assertEquals(expected, result)
        verify(exactly = 1) { dao.getCategories() }
    }

    @Test
    fun `getGamesByCategory delegates to dao with correct category`() = runTest {
        every { dao.getGamesByCategory("Action") } returns flowOf(listOf(game1))

        val result = repo.getGamesByCategory("Action").first()

        assertEquals(listOf(game1), result)
        verify(exactly = 1) { dao.getGamesByCategory("Action") }
    }

    @Test
    fun `searchGames delegates to dao with query`() = runTest {
        every { dao.searchGames("Game") } returns flowOf(listOf(game1, game2))

        val result = repo.searchGames("Game").first()

        assertEquals(2, result.size)
        verify(exactly = 1) { dao.searchGames("Game") }
    }

    @Test
    fun `searchGames returns empty list when no match`() = runTest {
        every { dao.searchGames("xyz") } returns flowOf(emptyList())

        val result = repo.searchGames("xyz").first()

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getGameById returns correct game`() = runTest {
        every { dao.getGameById(1) } returns flowOf(game1)

        val result = repo.getGameById(1).first()

        assertEquals(game1, result)
    }

    @Test
    fun `getGameById returns null for missing id`() = runTest {
        every { dao.getGameById(999) } returns flowOf(null)

        val result = repo.getGameById(999).first()

        assertNull(result)
    }

    @Test
    fun `getStats returns correct stats`() = runTest {
        val stats = Stats(totalGames = 100, totalCategories = 10)
        every { dao.getStats() } returns flowOf(stats)

        val result = repo.getStats().first()

        assertEquals(100, result.totalGames)
        assertEquals(10, result.totalCategories)
    }
}
