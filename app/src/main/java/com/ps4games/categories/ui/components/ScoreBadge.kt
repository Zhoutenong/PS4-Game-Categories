package com.ps4games.categories.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ps4games.categories.ui.theme.ScoreGreen
import com.ps4games.categories.ui.theme.ScoreOrange
import com.ps4games.categories.ui.theme.ScoreRed
import com.ps4games.categories.ui.theme.ScoreNone

@Composable
fun ScoreBadge(score: Int, modifier: Modifier = Modifier) {
    val (textColor, bgColor, label) = when {
        score <= 0 -> Triple(ScoreNone, ScoreNone.copy(alpha = 0.15f), "N/A")
        score >= 85 -> Triple(ScoreGreen, ScoreGreen.copy(alpha = 0.15f), "$score")
        score >= 70 -> Triple(ScoreOrange, ScoreOrange.copy(alpha = 0.15f), "$score")
        else -> Triple(ScoreRed, ScoreRed.copy(alpha = 0.15f), "$score")
    }
    Box(
        modifier = modifier
            .widthIn(min = 40.dp)
            .clip(MaterialTheme.shapes.small)
            .background(bgColor)
            .padding(horizontal = 6.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = textColor)
    }
}
