package com.example.youtubekotlin.ui.playlist

import androidx.lifecycle.LiveData
import com.example.youtubekotlin.App
import com.example.youtubekotlin.core.ui.BaseViewModel
import com.example.youtubekotlin.data.remote.model.Playlists
import com.example.youtubekotlin.core.network.result.Resource


class PlaylistViewModel: BaseViewModel() {
    fun getPlaylists(): LiveData<Resource<out Playlists>> {
        return App().repository.getPlaylists()
    }
}