package com.ps4games.categories.ui.search

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
class SearchViewModelTest {

    @get:Rule
    val instantTaskRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repo: GameRepository
    private lateinit var viewModel: SearchViewModel

    private val game1 = GameEntity(1, "ゴッド・オブ・ウォー", "God of War", 94, listOf("Action"), "Action")
    private val game2 = GameEntity(2, "ゴースト・オブ・ツシマ", "Ghost of Tsushima", 88, listOf("Action"), "Action")

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repo = mockk()
        every { repo.searchGames(any()) } returns flowOf(emptyList())
        viewModel = SearchViewModel(repo)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial query is empty`() {
        assertEquals("", viewModel.query.value)
    }

    @Test
    fun `initial results are empty`() = runTest {
        viewModel.results.test {
            assertEquals(emptyList<GameEntity>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `blank query returns empty results without calling repo`() = runTest {
        viewModel.results.test {
            assertEquals(emptyList<GameEntity>(), awaitItem())
            viewModel.query.value = "   "
            // blank query → flowOf(emptyList()) without repo call
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `non-blank query triggers search and returns results`() = runTest {
        every { repo.searchGames("God") } returns flowOf(listOf(game1))

        viewModel.results.test {
            assertEquals(emptyList<GameEntity>(), awaitItem())
            viewModel.query.value = "God"
            assertEquals(listOf(game1), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `query update replaces previous results`() = runTest {
        every { repo.searchGames("God") } returns flowOf(listOf(game1))
        every { repo.searchGames("Ghost") } returns flowOf(listOf(game2))

        viewModel.results.test {
            assertEquals(emptyList<GameEntity>(), awaitItem())
            viewModel.query.value = "God"
            assertEquals(listOf(game1), awaitItem())
            viewModel.query.value = "Ghost"
            assertEquals(listOf(game2), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `clearing query resets results to empty`() = runTest {
        every { repo.searchGames("God") } returns flowOf(listOf(game1))

        viewModel.results.test {
            assertEquals(emptyList<GameEntity>(), awaitItem())
            viewModel.query.value = "God"
            assertEquals(listOf(game1), awaitItem())
            viewModel.query.value = ""
            assertEquals(emptyList<GameEntity>(), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
