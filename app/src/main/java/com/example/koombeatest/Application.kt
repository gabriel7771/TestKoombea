package com.example.koombeatest

import android.app.Application
import com.couchbase.lite.CouchbaseLite
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        CouchbaseLite.init(this.applicationContext)
        if(BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}