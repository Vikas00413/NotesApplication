package org.example.notes.presentation.di

import org.example.notes.presentation.viewmodel.AddNoteViewModel
import org.example.notes.presentation.viewmodel.GetNotesViewModel
import org.example.notes.presentation.viewmodel.UpdateNoteViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::GetNotesViewModel)
    viewModelOf(::UpdateNoteViewModel)
    viewModelOf(::AddNoteViewModel)
}