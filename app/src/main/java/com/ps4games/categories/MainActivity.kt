package com.ps4games.categories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ps4games.categories.ui.navigation.AppNavGraph
import com.ps4games.categories.ui.theme.PS4GameTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PS4GameTheme {
                AppNavGraph(navController = rememberNavController())
            }
        }
    }
}
