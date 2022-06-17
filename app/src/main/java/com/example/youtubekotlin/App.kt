package com.example.youtubekotlin

import android.app.Application
import com.example.youtubekotlin.repository.Repository

class App: Application() {

    val repository: Repository by lazy {
        Repository()
    }
}