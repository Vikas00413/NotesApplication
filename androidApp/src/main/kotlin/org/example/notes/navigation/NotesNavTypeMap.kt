package org.example.notes.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlin.reflect.typeOf
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.example.notes.domain.model.Notes

private val notesJson = Json {
    ignoreUnknownKeys = true
    encodeDefaults = true
}

val NotesNavType = object : NavType<Notes>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Notes? {
        return bundle.getString(key)?.let { notesJson.decodeFromString<Notes>(it) }
    }

    override fun parseValue(value: String): Notes {
        return notesJson.decodeFromString(Uri.decode(value))
    }

    override fun serializeAsValue(value: Notes): String {
        return Uri.encode(notesJson.encodeToString(value))
    }

    override fun put(bundle: Bundle, key: String, value: Notes) {
        bundle.putString(key, notesJson.encodeToString(value))
    }
}

val notesNavTypeMap = mapOf(
    typeOf<Notes>() to NotesNavType,
)
