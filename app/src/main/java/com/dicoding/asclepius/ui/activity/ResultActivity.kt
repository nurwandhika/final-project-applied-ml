package com.dicoding.asclepius.ui.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.ui.adapter.HealthNewsAdapter
import com.dicoding.asclepius.ui.viewmodel.ResultViewModel

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val result: String? by lazy { intent.getStringExtra(EXTRA_RESULT) }
    private val resultImageUri: Uri by lazy { Uri.parse(intent.getStringExtra(EXTRA_IMAGE)) }
    private val viewModel: ResultViewModel by viewModels<ResultViewModel> { ResultViewModel.Factory() }
    private val articleAdapter = HealthNewsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupUI()
    }

    private fun setupObservers() {
        observeLoadingStatus()
        observeArticleList()
    }

    private fun observeLoadingStatus() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressIndicator.isVisible = isLoading
        }
    }

    private fun observeArticleList() {
        viewModel.articleList.observe(this) { articles ->
            articleAdapter.submitList(articles)
        }
    }

    private fun setupUI() {
        setupToolbar()
        setupResult()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        binding.resultToolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupResult() {
        binding.resultImage.setImageURI(resultImageUri)
        binding.resultText.text = result
    }

    private fun setupRecyclerView() {
        binding.articlesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ResultActivity)
            adapter = articleAdapter
        }
    }

    companion object {
        const val EXTRA_RESULT = "extra_result"
        const val EXTRA_IMAGE = "extra_image"
    }
}