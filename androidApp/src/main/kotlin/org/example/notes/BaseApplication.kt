package org.example.notes

import android.app.Application
import org.example.notes.di.androidPlatformDataModule
import org.example.notes.di.dataModule
import org.example.notes.di.initKoin
import org.example.notes.di.useCaseModule
import org.example.notes.presentation.di.viewModelModule

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
                modules(
                    androidPlatformDataModule(this@BaseApplication),
                    dataModule,
                    useCaseModule,
                    viewModelModule,
                )

        }
    }
}