package com.ps4games.categories.data.db

import org.junit.Assert.*
import org.junit.Test

class TagsConverterTest {

    private val converter = TagsConverter()

    @Test
    fun `fromString with normal csv returns list`() {
        val result = converter.fromString("Action,RPG,Adventure")
        assertEquals(listOf("Action", "RPG", "Adventure"), result)
    }

    @Test
    fun `fromString with spaces trims each tag`() {
        val result = converter.fromString("Action, RPG , Adventure")
        assertEquals(listOf("Action", "RPG", "Adventure"), result)
    }

    @Test
    fun `fromString with null returns empty list`() {
        assertEquals(emptyList<String>(), converter.fromString(null))
    }

    @Test
    fun `fromString with blank string returns empty list`() {
        assertEquals(emptyList<String>(), converter.fromString(""))
        assertEquals(emptyList<String>(), converter.fromString("   "))
    }

    @Test
    fun `fromString with single tag returns single-element list`() {
        assertEquals(listOf("Action"), converter.fromString("Action"))
    }

    @Test
    fun `toString joins list with comma`() {
        assertEquals("Action,RPG,Adventure", converter.toString(listOf("Action", "RPG", "Adventure")))
    }

    @Test
    fun `toString with empty list returns empty string`() {
        assertEquals("", converter.toString(emptyList()))
    }

    @Test
    fun `toString with single element returns that element`() {
        assertEquals("Action", converter.toString(listOf("Action")))
    }

    @Test
    fun `roundtrip fromString then toString is stable`() {
        val original = "Action,RPG,Adventure"
        val result = converter.toString(converter.fromString(original))
        assertEquals(original, result)
    }

    @Test
    fun `roundtrip toString then fromString is stable`() {
        val original = listOf("Action", "RPG", "Adventure")
        val result = converter.fromString(converter.toString(original))
        assertEquals(original, result)
    }
}
