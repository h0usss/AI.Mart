package com.h0uss.aimart

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Graph.provide(applicationContext)
    }
}