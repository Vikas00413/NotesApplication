package org.example.notes

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.example.notes.data.SampleNotesSeeder
import org.example.notes.di.androidPlatformDataModule
import org.example.notes.di.dataModule
import org.example.notes.di.initKoin
import org.example.notes.di.sampleDataModule
import org.example.notes.di.useCaseModule
import org.example.notes.presentation.di.viewModelModule
import org.koin.core.context.GlobalContext

class BaseApplication: Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        initKoin {
                modules(
                    androidPlatformDataModule(this@BaseApplication),
                    dataModule,
                    useCaseModule,
                    viewModelModule,
                    sampleDataModule,
                )

        }

        applicationScope.launch {
            GlobalContext.get().get<SampleNotesSeeder>().seedIfEmpty()
        }
    }
}