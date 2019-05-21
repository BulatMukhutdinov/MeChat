package tat.mukhutdinov.mechat

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import tat.mukhutdinov.mechat.main.di.InjectionModule
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        setupKoin()

        setupTimber()
    }

    private fun setupKoin() {
        startKoin {
            androidContext(this@App)
            modules(InjectionModule.module)
        }
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}