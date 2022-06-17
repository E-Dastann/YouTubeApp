package com.example.youtubekotlin.data.remote.model

import com.example.youtubekotlin.data.remote.model.Item
import com.example.youtubekotlin.data.remote.model.PageInfo

data class Playlist(
    val etag: String,
    val items: List<Item>,
    val kind: String,
    val nextPageToken: String,
    val pageInfo: PageInfo
)