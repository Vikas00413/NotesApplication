package org.example.notes.di

import android.content.Context
import org.example.notes.data.local.database.NoteDataBase
import org.example.notes.database.createNotesDatabase
import org.koin.dsl.module


fun androidPlatformDataModule(context: Context) = module {
    single<NoteDataBase> { createNotesDatabase(context.applicationContext) }
}
