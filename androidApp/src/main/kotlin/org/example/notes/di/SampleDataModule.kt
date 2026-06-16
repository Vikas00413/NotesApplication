package org.example.notes.di

import org.example.notes.data.SampleNotesSeeder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val sampleDataModule = module {
    singleOf(::SampleNotesSeeder)
}
