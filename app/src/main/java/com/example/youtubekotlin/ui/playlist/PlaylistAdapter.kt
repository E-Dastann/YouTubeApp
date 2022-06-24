package com.example.youtubekotlin.ui.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.youtubekotlin.R
import com.example.youtubekotlin.data.remote.model.Item

import com.example.youtubekotlin.databinding.ItemPlaylistBinding

class PlaylistAdapter(
    private val list: List<Item>, private val onItemClick: (itemsId: String, String, String) -> Unit?
) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(private val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(items: Item) {
            Glide.with(binding.root).load(items.snippet.thumbnails.default.url)
                .into(binding.imageEv)

            binding.playlistNameTv.text = items.snippet.title
            binding.playlistCountTv.text =
                String.format("${items.contentDetails.itemCount} ${itemView.context.getString(R.string.video_series)}")
            itemView.setOnClickListener {
                onItemClick(items.id, items.snippet.title, items.snippet.description)
            }
        }
    }

}

