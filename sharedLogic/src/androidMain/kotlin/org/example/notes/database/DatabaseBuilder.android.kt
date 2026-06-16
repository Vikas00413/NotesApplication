package org.example.notes.database

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import org.example.notes.data.local.database.NoteDataBase

fun createNotesDatabase(context: Context): NoteDataBase {

    val dbFile = context.getDatabasePath("notes.db")
    return Room.databaseBuilder<NoteDataBase>(
        context = context.applicationContext,
        name = dbFile.absolutePath,
    )
        .setDriver(
            BundledSQLiteDriver()
        ).setQueryCoroutineContext(Dispatchers.IO).build()
}