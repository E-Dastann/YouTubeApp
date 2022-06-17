package com.example.youtubekotlin.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.example.youtubekotlin.BuildConfig.API_KEY
import com.example.youtubekotlin.core.network.RetrofitClient
import com.example.youtubekotlin.core.network.result.Resource
import com.example.youtubekotlin.core.network.result.Resource.Companion.loading
import com.example.youtubekotlin.core.network.result.Resource.Companion.success
import com.example.youtubekotlin.data.remote.ApiService
import com.example.youtubekotlin.data.remote.model.PlaylistItems
import com.example.youtubekotlin.data.remote.model.Playlists
import com.example.youtubekotlin.utils.Constant
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository {

    private val apiService: ApiService by lazy {
        RetrofitClient.create()
    }

    fun getPlaylists(): LiveData<Resource<out Playlists>> = liveData(Dispatchers.IO) {

        emit(loading())
        val response =
            apiService
                .getPlaylists(
                    Constant.part,
                    Constant.channelId,
                    API_KEY,
                    Constant.maxResult
                )

        emit(
            (if (response.isSuccessful) success(response.body()!!) else Resource.error(
                response.message(),
                null
            ))
        )

    }


    fun getPlaylistItems(playlistId: String): LiveData<Resource<out PlaylistItems>> = liveData (Dispatchers.IO) {
        emit(loading())
        val response =
            apiService
                .getPlaylistItems(
                    Constant.part,
                    playlistId,
                    API_KEY,
                    Constant.maxResult
                )

        emit(
            (if (response.isSuccessful) success(response.body()!!) else Resource.error(response.message(), null)))


    }
}


