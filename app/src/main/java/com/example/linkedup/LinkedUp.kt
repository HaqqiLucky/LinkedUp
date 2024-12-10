package com.example.linkedup

import android.app.Application
import android.content.Context

class LinkedUp : Application() {
    // Static reference for global access to the context
    companion object {
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}