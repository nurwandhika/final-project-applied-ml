package com.dicoding.asclepius.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.asclepius.data.model.NewsItem
import com.dicoding.asclepius.databinding.ItemArticlesRowBinding

class HealthNewsAdapter :
    ListAdapter<NewsItem, HealthNewsAdapter.ArticleViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = inflateBinding(parent)
        return ArticleViewHolder(binding)
    }

    private fun inflateBinding(parent: ViewGroup): ItemArticlesRowBinding {
        return ItemArticlesRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    inner class ArticleViewHolder(private val binding: ItemArticlesRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(newsItem: NewsItem) {
            bindImage(newsItem.newsImageUrl)
            bindTitle(newsItem.newsTitle)
            bindDescription(newsItem.newsDescription)
        }

        private fun bindImage(imageUrl: String?) {
            imageUrl?.let { loadImage(it) }
        }

        private fun loadImage(imageUrl: String) {
            Glide.with(binding.root)
                .load(imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.ivArticle)
        }

        private fun bindTitle(title: String?) {
            title?.let { setTitle(it) }
        }

        private fun setTitle(title: String) {
            binding.tvArticleTitle.text = title
        }

        private fun bindDescription(description: String?) {
            description?.let { setDescription(it) }
        }

        private fun setDescription(description: String) {
            binding.tvArticleDesc.text = description
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsItem>() {
            override fun areItemsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: NewsItem, newItem: NewsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}