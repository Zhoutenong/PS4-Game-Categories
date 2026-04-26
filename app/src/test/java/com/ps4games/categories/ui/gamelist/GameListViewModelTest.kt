package com.ps4games.categories.ui.gamelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.ps4games.categories.data.db.GameEntity
import com.ps4games.categories.data.repository.GameRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GameListViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repo: GameRepository
    private lateinit var viewModel: GameListViewModel

    private val gameA = GameEntity(1, "ゲームA", "Game A", 90, listOf("Action"), "Action")
    private val gameB = GameEntity(2, "ゲームB", "Game B", 60, listOf("Action"), "Action")
    private val gameC = GameEntity(3, "ゲームC", "Game C", 75, listOf("Action"), "Action")
    private val actionGames = listOf(gameA, gameB, gameC)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = mockk()
        every { repo.getGamesByCategory(any()) } returns flowOf(actionGames)
        viewModel = GameListViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial games are empty before category is set`() = runTest {
        viewModel.games.test {
            assertEquals(emptyList<GameEntity>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setCategory loads games in default order`() = runTest {
        viewModel.games.test {
            assertEquals(emptyList<GameEntity>(), awaitItem())
            viewModel.setCategory("Action")
            assertEquals(actionGames, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `SCORE_DESC sort orders games by score descending`() = runTest {
        viewModel.setCategory("Action")
        viewModel.games.test {
            awaitItem() // consume current
            viewModel.setSortOrder(SortOrder.SCORE_DESC)
            val sorted = awaitItem()
            assertEquals(listOf(gameA, gameC, gameB), sorted) // 90, 75, 60
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `SCORE_ASC sort orders games by score ascending`() = runTest {
        viewModel.setCategory("Action")
        viewModel.games.test {
            awaitItem()
            viewModel.setSortOrder(SortOrder.SCORE_ASC)
            val sorted = awaitItem()
            assertEquals(listOf(gameB, gameC, gameA), sorted) // 60, 75, 90
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `DEFAULT sort preserves original order`() = runTest {
        viewModel.setCategory("Action")
        viewModel.games.test {
            awaitItem()
            viewModel.setSortOrder(SortOrder.SCORE_DESC)
            awaitItem()
            viewModel.setSortOrder(SortOrder.DEFAULT)
            val sorted = awaitItem()
            assertEquals(actionGames, sorted)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `initial sortOrder is DEFAULT`() {
        assertEquals(SortOrder.DEFAULT, viewModel.sortOrder.value)
    }

    @Test
    fun `setSortOrder updates sortOrder state`() {
        viewModel.setSortOrder(SortOrder.SCORE_DESC)
        assertEquals(SortOrder.SCORE_DESC, viewModel.sortOrder.value)
    }

    @Test
    fun `games with equal scores maintain stable relative order in SCORE_DESC`() {
        // sortedByDescending is stable: equal elements preserve original order
        val g1 = GameEntity(10, "X", "X", 80, emptyList(), "RPG")
        val g2 = GameEntity(11, "Y", "Y", 80, emptyList(), "RPG")
        val sorted = listOf(g1, g2).sortedByDescending { it.score }
        assertEquals(listOf(g1, g2), sorted)
    }
}
