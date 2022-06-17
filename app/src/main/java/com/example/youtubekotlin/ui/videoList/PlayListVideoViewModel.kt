package com.example.youtubekotlin.ui.videoList
import androidx.lifecycle.LiveData
import com.example.youtubekotlin.App
import com.example.youtubekotlin.core.network.result.Resource
import com.example.youtubekotlin.core.ui.BaseViewModel
import com.example.youtubekotlin.data.remote.model.PlaylistItems

class PlayListVideoViewModel:BaseViewModel() {
        fun  getPlaylistItems(playlistId:String): LiveData<Resource<out PlaylistItems>> {
            return App().repository.getPlaylistItems(playlistId)
        }

    }
