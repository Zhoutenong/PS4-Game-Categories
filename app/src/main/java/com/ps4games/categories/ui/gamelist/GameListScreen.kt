package com.ps4games.categories.ui.gamelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ps4games.categories.data.db.GameEntity
import com.ps4games.categories.ui.components.ScoreBadge

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameListScreen(
    category: String,
    onGameClick: (Int) -> Unit,
    onBack: () -> Unit,
    vm: GameListViewModel = hiltViewModel()
) {
    LaunchedEffect(category) { vm.setCategory(category) }

    val games by vm.games.collectAsState()
    val sortOrder by vm.sortOrder.collectAsState()
    var showSortMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(category, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    Box {
                        IconButton(onClick = { showSortMenu = true }) {
                            Icon(Icons.Default.Sort, contentDescription = "排序")
                        }
                        DropdownMenu(expanded = showSortMenu, onDismissRequest = { showSortMenu = false }) {
                            DropdownMenuItem(text = { Text("默认排序") }, onClick = { vm.setSortOrder(SortOrder.DEFAULT); showSortMenu = false })
                            DropdownMenuItem(text = { Text("按评分降序") }, onClick = { vm.setSortOrder(SortOrder.SCORE_DESC); showSortMenu = false })
                            DropdownMenuItem(text = { Text("按评分升序") }, onClick = { vm.setSortOrder(SortOrder.SCORE_ASC); showSortMenu = false })
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(games, key = { it.id }) { game ->
                GameListItem(game = game, onClick = { onGameClick(game.id) })
                HorizontalDivider(color = MaterialTheme.colorScheme.outline, thickness = 0.5.dp)
            }
        }
    }
}

@Composable
private fun GameListItem(game: GameEntity, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(game.name, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            if (game.name_en.isNotBlank()) {
                Text(game.name_en, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
        Spacer(Modifier.width(8.dp))
        ScoreBadge(score = game.score)
    }
}
