package com.ps4games.categories.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ps4games.categories.ui.detail.DetailScreen
import com.ps4games.categories.ui.gamelist.GameListScreen
import com.ps4games.categories.ui.home.HomeScreen
import com.ps4games.categories.ui.search.SearchScreen
import java.net.URLDecoder
import java.net.URLEncoder

object Routes {
    const val HOME = "home"
    const val GAME_LIST = "games/{category}"
    const val SEARCH = "search"
    const val DETAIL = "detail/{gameId}"

    fun gameList(category: String) = "games/${URLEncoder.encode(category, "UTF-8")}"
    fun detail(gameId: Int) = "detail/$gameId"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        composable(Routes.HOME) { entry ->
            HomeScreen(
                onCategoryClick = { category ->
                    if (navController.currentBackStackEntry == entry) {
                        navController.navigate(Routes.gameList(category))
                    }
                },
                onSearchClick = { navController.navigate(Routes.SEARCH) }
            )
        }
        composable(
            route = Routes.GAME_LIST,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStack ->
            val category = URLDecoder.decode(backStack.arguments?.getString("category") ?: "", "UTF-8")
            GameListScreen(
                category = category,
                onGameClick = { gameId ->
                    if (navController.currentBackStackEntry == backStack) {
                        navController.navigate(Routes.detail(gameId))
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.SEARCH) {
            SearchScreen(
                onGameClick = { gameId -> navController.navigate(Routes.detail(gameId)) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("gameId") { type = NavType.IntType })
        ) { backStack ->
            val gameId = backStack.arguments?.getInt("gameId") ?: 0
            DetailScreen(
                gameId = gameId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
