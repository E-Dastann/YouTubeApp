package com.example.youtubekotlin.ui.playlist_detail
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.youtubekotlin.core.network.result.Status
import com.example.youtubekotlin.core.ui.BaseActivity
import com.example.youtubekotlin.data.remote.model.Item
import com.example.youtubekotlin.databinding.ActivityPlaylistDetailBinding
import com.example.youtubekotlin.ui.playlist.PlaylistActivity
import com.example.youtubekotlin.ui.videoList.ActivityVideo
import com.example.youtubekotlin.utils.NetworkStatus
import com.example.youtubekotlin.utils.NetworkStatusHelper

class PlaylistDetailActivity :
    BaseActivity<ActivityPlaylistDetailBinding, PlaylistDetailViewModel>() {
    companion object {
        const val  VIDEO_ID= "videoId"
        const val VIDEO_TITLE =  "videoTitle"
        const val VIDEO_DESC= "videoDesc"


    }

    override val viewModel: PlaylistDetailViewModel by lazy {
        ViewModelProvider(this)[PlaylistDetailViewModel::class.java]
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityPlaylistDetailBinding {
        return ActivityPlaylistDetailBinding.inflate(inflater)
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
                binding.recyclerDetail.visibility = View.VISIBLE
                binding.networkLayout.root.visibility = View.GONE
                binding.appBar.visibility = View.VISIBLE

                initVM()
            } else {
                binding.recyclerDetail.visibility = View.GONE
                binding.networkLayout.root.visibility = View.VISIBLE
                binding.appBar.visibility = View.GONE
                binding.playButton.visibility = View.GONE
            }
        }

    }

    private fun initRecyclerView(playlistsList: List<Item>) {
        binding.recyclerDetail.adapter = PlaylistDetailAdapter(playlistsList ,this::onItemClick)
    }

    private  fun onItemClick (videoId: String,videoTitle:String,videoListDesc:String){
        Intent(this, ActivityVideo::class.java).apply {
            putExtra(VIDEO_ID,videoId)
            putExtra(VIDEO_TITLE,videoTitle)
            putExtra(VIDEO_DESC, videoListDesc)
            startActivity(this)
        }


    }



    override fun initView() {
        super.initView()

        binding.mainTitle.text = intent.getStringExtra(PlaylistActivity.titleData)
        binding.secondTitle.text = intent.getStringExtra(PlaylistActivity.descData)
    }

    override fun initListener() {

        super.initListener()
        binding.back.setOnClickListener {
            onBackPressed()
        }

    }
}