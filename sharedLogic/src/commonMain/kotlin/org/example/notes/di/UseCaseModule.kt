package org.example.notes.di


import org.example.notes.domain.usecase.AddNoteUseCase
import org.example.notes.domain.usecase.DeleteNotesUseCase
import org.example.notes.domain.usecase.GetAllNotesUseCase
import org.example.notes.domain.usecase.MarkNotesDoneUseCase
import org.example.notes.domain.usecase.MarkNotesNotDoneUseCase
import org.example.notes.domain.usecase.UpdateNotesUseCase
import org.koin.core.module.dsl.singleOf

import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::GetAllNotesUseCase)
    singleOf(::DeleteNotesUseCase)
    singleOf(::MarkNotesDoneUseCase)
    singleOf(::MarkNotesNotDoneUseCase)
    singleOf(::UpdateNotesUseCase)
    singleOf(::AddNoteUseCase)
}