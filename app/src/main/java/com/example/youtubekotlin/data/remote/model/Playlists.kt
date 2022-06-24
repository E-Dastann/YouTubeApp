package com.example.youtubekotlin.data.remote.model

data class Playlists (
    val kind: String? = null,
    val items: List<Item>,
)
data class PageInfo(
    val resultsPerPage: Int,
    val totalResults: Int
)

data class Snippet(
    val channelId: String,
    val channelTitle: String,
    val description: String,
    val localized: Localized,
    val publishedAt: String,
    val thumbnails: Thumbnails,
    val title: String,
    var resourceId: ResourceId,
)

data class ResourceId(
    var kind: String,
    var videoId: String
)