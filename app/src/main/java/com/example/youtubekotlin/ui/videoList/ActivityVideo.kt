package com.example.youtubekotlin.ui.videoList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar

import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.youtubekotlin.R

import com.example.youtubekotlin.core.network.result.Status
import com.example.youtubekotlin.core.ui.BaseActivity
import com.example.youtubekotlin.data.remote.model.Item
import com.example.youtubekotlin.databinding.ActivityVideoBinding
import com.example.youtubekotlin.ui.playlist.PlaylistActivity
import com.example.youtubekotlin.ui.playlist_detail.PlaylistDetailActivity
import com.example.youtubekotlin.ui.playlist_detail.PlaylistDetailViewModel
import com.example.youtubekotlin.utils.NetworkStatus
import com.example.youtubekotlin.utils.NetworkStatusHelper
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView


class ActivityVideo :
    BaseActivity<ActivityVideoBinding, PlaylistDetailViewModel>(), Player.Listener {
    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private lateinit var progressBar: ProgressBar


    override val viewModel: PlaylistDetailViewModel by lazy {
        ViewModelProvider(this)[PlaylistDetailViewModel::class.java]
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityVideoBinding {
        return ActivityVideoBinding.inflate(inflater)
    }

    override fun initViewModel() {
        initVM()
    }

    private fun initVM() {
        intent.getStringExtra(PlaylistActivity.idPaPda)?.let { it ->
            viewModel.getPlaylistItems(it).observe(this) {
                when (it.status) {
                    Status.SUCCESS -> {
                        if (it.data != null) {

                            viewModel.loading.postValue(false)
                            initRecyclerView(it.data.items)

                        } else {
                            Log.e("Error1", "error 1")
                        }
                    }
                    Status.ERROR -> {
                        viewModel.loading.postValue(false)
                        Log.e("Error2", "error 2")
                        Toast.makeText(this, it.msg, Toast.LENGTH_SHORT).show()
                    }
                    Status.LOADING -> {
                        viewModel.loading.postValue(true)
                    }
                }
            }
        }
    }

    override fun checkInternet() {
        NetworkStatusHelper(this).observe(this) {
            if (it == NetworkStatus.Available) {
                binding.networkLayout.root.visibility = View.GONE
                binding.toolBarLayout.visibility = View.VISIBLE

                initVM()
            } else {
                binding.networkLayout.root.visibility = View.VISIBLE
                binding.toolBarLayout.visibility = View.GONE
            }
        }

    }

    private fun initRecyclerView(playlistsList: List<Item>) {

    }

    override fun initView() {
        super.initView()
        setUpPlayer()
        addMp3Files()
        addMp4Files()
        binding.videoTitle.text = intent.getStringExtra(PlaylistDetailActivity.VIDEO_TITLE)
        binding.videoDesc.text = intent.getStringExtra(PlaylistDetailActivity.VIDEO_DESC)
    }

    override fun initListener() {

        super.initListener()
        binding.back.setOnClickListener {
            onBackPressed()
        }

    }

    fun setUpPlayer() {
        player = ExoPlayer.Builder(this).build()
        playerView = findViewById(R.id.exo_player)
        playerView.player =player
        player.addListener(this)


    }

    private fun addMp4Files() {
        val mediaItem = MediaItem.fromUri(getString(R.string.media_url_mp4))
        player.addMediaItem(mediaItem)
        player.prepare()
    }

    private fun addMp3Files() {
        val mediaItem = MediaItem.fromUri(getString(R.string.test_mp3))
        player.addMediaItem(mediaItem)
        player.prepare()

    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        progressBar = findViewById(R.id.progress_bar)
        when (playbackState) {
            Player.STATE_BUFFERING -> {
                progressBar.visibility = View.VISIBLE
            }
            Player.STATE_READY -> {
                progressBar.visibility = View.INVISIBLE
            }

        }
    }


}
