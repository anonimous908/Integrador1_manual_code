package org.example.project

import android.app.Application
import org.example.project.di.initKoin
import org.koin.android.ext.koin.androidContext

class CodeNestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            initKoin {
                androidContext(this@CodeNestApplication)
            }
        } catch (e: Exception) {
            // Ya inicializado
        }
    }
}
