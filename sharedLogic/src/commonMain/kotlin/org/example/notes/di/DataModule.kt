package org.example.notes.di

import org.example.notes.data.local.database.NoteDataBase
import org.example.notes.data.repository.NoteRepositoryImpl
import org.example.notes.domain.repository.NotesRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single { get<NoteDataBase>().noteDao() }
    singleOf(::NoteRepositoryImpl) bind NotesRepository::class
}