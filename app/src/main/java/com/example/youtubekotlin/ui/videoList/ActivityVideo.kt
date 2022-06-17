package com.example.youtubekotlin.ui.videoList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.youtubekotlin.core.extentions.showToast
import com.example.youtubekotlin.core.network.result.Status
import com.example.youtubekotlin.core.ui.BaseActivity
import com.example.youtubekotlin.data.remote.model.Item
import com.example.youtubekotlin.databinding.ActivityVideoBinding
import com.example.youtubekotlin.ui.playlist.PlaylistActivity
import com.example.youtubekotlin.ui.playlist_detail.PlaylistDetailActivity
import com.example.youtubekotlin.ui.playlist_detail.PlaylistDetailViewModel
import com.example.youtubekotlin.utils.NetworkStatus
import com.example.youtubekotlin.utils.NetworkStatusHelper


class ActivityVideo :
    BaseActivity<ActivityVideoBinding, PlaylistDetailViewModel>() {

    override val viewModel: PlaylistDetailViewModel by lazy {
        ViewModelProvider(this)[PlaylistDetailViewModel::class.java]
    }

    override fun inflateViewBinding(inflater: LayoutInflater): ActivityVideoBinding {
        return ActivityVideoBinding.inflate(inflater)
    }

    override fun initViewModel() {
        initVM()
        showToast("iuuuuu")
        showToast("iuuuuu")
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

        binding.videoTitle.text = intent.getStringExtra(PlaylistDetailActivity.VIDEO_TITLE)
        binding.videoDesc.text = intent.getStringExtra(PlaylistDetailActivity.VIDEO_DESC)
    }

    override fun initListener() {

        super.initListener()
        binding.back.setOnClickListener {
            onBackPressed()
        }

    }
}
