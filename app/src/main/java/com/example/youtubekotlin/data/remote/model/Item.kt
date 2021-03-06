package com.example.youtubekotlin.data.remote.model

data class Item(
    val contentDetails: ContentDetails,
    val etag: String,
    val id: String,
    val kind: String,
    val snippet: Snippet,
    val resourceId: Unit
)
