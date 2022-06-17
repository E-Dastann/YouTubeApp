package com.example.youtubekotlin.data.remote.model

import com.example.youtubekotlin.data.remote.model.Item

data class Playlists (
    val kind: String? = null,
    val items: List<Item>,
)