package com.example.youtubekotlin.ui.videoList

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.youtubekotlin.BuildConfig
import com.example.youtubekotlin.R
import com.example.youtubekotlin.core.ui.BaseActivity
import com.example.youtubekotlin.data.remote.model.Item
import com.example.youtubekotlin.databinding.ActivityVideoBinding
import com.example.youtubekotlin.ui.playlist_detail.PlaylistDetailActivity
import com.example.youtubekotlin.ui.playlist_detail.PlaylistDetailViewModel
import com.example.youtubekotlin.utils.NetworkStatus
import com.example.youtubekotlin.utils.NetworkStatusHelper
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource


class ActivityVideo :
    BaseActivity<ActivityVideoBinding, PlayListVideoViewModel>(), Player.Listener {
    private lateinit var player: ExoPlayer
    private lateinit var progressBar: ProgressBar
    private lateinit var videoSource:ProgressiveMediaSource
    private lateinit var audioSource:ProgressiveMediaSource
    private var videoId: String? = null


    override val viewModel: PlayListVideoViewModel by lazy {
        ViewModelProvider(this)[PlayListVideoViewModel::class.java]
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityVideoBinding {
        return ActivityVideoBinding.inflate(inflater)
    }

    override fun initViewModel() {
        initVM()
    }

    private fun initVM() {
        videoId =  intent.getStringExtra(PlaylistDetailActivity.VIDEO_ID).toString()
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

    private fun initRecyclerView(playlistsList: List<Item>) {}

    override fun initView() {
        super.initView()
        downloadVideo()
        getDataIntent()
    }

    private fun getDataIntent() {
        binding.videoTitle.text = intent.getStringExtra(PlaylistDetailActivity.VIDEO_TITLE).toString()
        binding.videoDesc.text = intent.getStringExtra(PlaylistDetailActivity.VIDEO_DESC).toString()
    }

    @SuppressLint("StaticFieldLeak")
    private fun downloadVideo(){
        object : YouTubeExtractor(this) {
            override fun onExtractionComplete(ytFiles: SparseArray<YtFile>?, vMeta: VideoMeta?) {
                if (ytFiles != null) {
                    val videoTag = 134
                    val audioTag = 140
                    val audioUrl = ytFiles[audioTag].url
                    val videoUrl = ytFiles[videoTag].url
                    setUpPlayer(videoUrl,audioUrl)
                }
            }
        }.extract(BuildConfig.BASE_YOUTUBE+videoId )
        Log.d("hello", "${BuildConfig.BASE_YOUTUBE+videoId} ")
    }

    override fun initListener() {
        super.initListener()
        binding.back.setOnClickListener {
            onBackPressed()

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("Seek Time", player.currentPosition)
        outState.putInt("mediaItem", player.currentMediaItemIndex)
    }


    fun setUpPlayer(videoUrl: String, audioUrl: String) {
        buildMediaSource(videoUrl,audioUrl)
        player = ExoPlayer.Builder(this).build()
        binding.exoPlayer.player = player
        player.setMediaSource(MergingMediaSource(videoSource,audioSource))
        player.addListener(this)
        player.prepare()
    }

    private fun buildMediaSource(videoUrl: String, audioUrl: String) {
        videoSource =  ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(videoUrl))
       audioSource =  ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
            .createMediaSource(MediaItem.fromUri(audioUrl))
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        super.onPlaybackStateChanged(playbackState)
        progressBar = findViewById(R.id.progress_bar)
        when (playbackState) {
            Player.STATE_BUFFERING -> progressBar.visibility = View.VISIBLE
            Player.STATE_READY -> progressBar.visibility = View.INVISIBLE
            Player.STATE_ENDED -> {}
            Player.STATE_IDLE -> {}
        }
    }


}