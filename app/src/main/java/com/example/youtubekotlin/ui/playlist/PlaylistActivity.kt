package com.example.youtubekotlin.ui.playlist

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.RemoteViewsService
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.youtubekotlin.databinding.ActivityPlaylistBinding
import com.example.youtubekotlin.core.network.result.Status
import com.example.youtubekotlin.core.ui.BaseActivity
import com.example.youtubekotlin.data.remote.model.Item

import com.example.youtubekotlin.ui.playlist_detail.PlaylistDetailActivity
import com.example.youtubekotlin.utils.NetworkStatus
import com.example.youtubekotlin.utils.NetworkStatusHelper

class PlaylistActivity : BaseActivity<ActivityPlaylistBinding, PlaylistViewModel>() {


    override val viewModel: PlaylistViewModel by lazy {
        ViewModelProvider(this)[PlaylistViewModel::class.java]
    }

    override fun initViewModel() {

        viewModel.loading.observe(this) {
            binding.progressBar.isVisible = it
        }

        initVM()
    }

    private fun initRecyclerView(playlistsList: List<Item>) {
        binding.recyclerMain.adapter = PlaylistAdapter(playlistsList, this::onItemClick)
    }

    private fun onItemClick(channelId: String,playListTitle:String,playListDesc:String ) {
        Intent(this, PlaylistDetailActivity::class.java).apply {
            putExtra(idPaPda, channelId)
            putExtra(titleData,playListTitle)
            putExtra(descData,playListDesc)
            startActivity(this)
        }
    }

    private fun initVM() {
        viewModel.getPlaylists().observe(this) {
            when(it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        viewModel.loading.postValue(false)
                        initRecyclerView(it.data.items)
                    }
                }
                Status.ERROR -> {
                    viewModel.loading.postValue(false)
                    Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    viewModel.loading.postValue(true)
                }
            }
        }
    }

    private fun checkConnection() {
        NetworkStatusHelper(this).observe(this) {

            if (it == NetworkStatus.Available) {
                binding.recyclerMain.visibility = View.VISIBLE
                binding.networkLayout.root.visibility = View.GONE
                initVM()
            } else {
                binding.recyclerMain.visibility = View.GONE
                binding.networkLayout.root.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        const val idPaPda = "idPaPda"
        const val titleData ="tileData"
        const val descData= "descData"
    }

    override fun checkInternet() {
        checkConnection()

        binding.networkLayout.btnTryAgain.setOnClickListener {

            checkConnection()
        }
    }


    override fun inflateViewBinding(inflater: LayoutInflater): ActivityPlaylistBinding {
        return ActivityPlaylistBinding.inflate(inflater)
    }
}