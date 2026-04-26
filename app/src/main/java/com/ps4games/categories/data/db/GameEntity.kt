package com.ps4games.categories.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Entity(
    tableName = "games",
    indices = [
        Index(value = ["name", "name_en"], name = "idx_games_name"),
        Index(value = ["primary_category"], name = "idx_games_category")
    ]
)
@TypeConverters(TagsConverter::class)
data class GameEntity(
    @PrimaryKey val id: Int,
    val name: String,
    @ColumnInfo(defaultValue = "") val name_en: String,
    @ColumnInfo(defaultValue = "0") val score: Int,
    @ColumnInfo(defaultValue = "") val tags: List<String>,
    @ColumnInfo(defaultValue = "") val primary_category: String
)

class TagsConverter {
    @TypeConverter
    fun fromString(value: String?): List<String> =
        if (value.isNullOrBlank()) emptyList() else value.split(",").map { it.trim() }

    @TypeConverter
    fun toString(tags: List<String>): String = tags.joinToString(",")
}
