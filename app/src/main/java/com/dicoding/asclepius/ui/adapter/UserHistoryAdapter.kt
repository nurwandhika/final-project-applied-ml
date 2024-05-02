package com.dicoding.asclepius.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.asclepius.data.model.UserActivity
import com.dicoding.asclepius.databinding.ItemHistoryRowBinding

class UserHistoryAdapter :
    ListAdapter<UserActivity, UserHistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = inflateBinding(parent)
        return HistoryViewHolder(binding)
    }

    private fun inflateBinding(parent: ViewGroup): ItemHistoryRowBinding {
        return ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val userHistory = getItem(position)
        holder.bind(userHistory)
    }

    inner class HistoryViewHolder(private val binding: ItemHistoryRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(userActivity: UserActivity) {
            bindImage(userActivity.activityImageUri)
            bindResultText(userActivity.activityResults)
        }

        private fun bindImage(imageUri: String) {
            imageUri.let { loadImage(it) }
        }

        private fun loadImage(imageUri: String) {
            Glide.with(binding.root)
                .load(imageUri)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivHistory)
        }

        private fun bindResultText(result: String) {
            result.let { setResultText(it) }
        }

        private fun setResultText(result: String) {
            binding.tvResult.text = result
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserActivity>() {
            override fun areItemsTheSame(oldItem: UserActivity, newItem: UserActivity): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserActivity, newItem: UserActivity): Boolean {
                return oldItem == newItem
            }
        }
    }
}