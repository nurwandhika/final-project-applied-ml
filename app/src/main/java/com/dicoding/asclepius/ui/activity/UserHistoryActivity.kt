package com.dicoding.asclepius.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.model.UserActivity
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.ui.adapter.UserHistoryAdapter
import com.dicoding.asclepius.ui.viewmodel.UserHistoryViewModel

class UserHistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private val historyAdapter = UserHistoryAdapter()

    private val historyViewModel: UserHistoryViewModel by viewModels<UserHistoryViewModel> {
        UserHistoryViewModel.Factory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupUI()
    }

    private fun setupObservers() {
        observeUserHistories()
    }

    private fun observeUserHistories() {
        historyViewModel.getAllHistories().observe(this) { userHistories ->
            updateHistoryList(userHistories)
        }
    }

    private fun updateHistoryList(userHistories: List<UserActivity>) {
        historyAdapter.submitList(userHistories as ArrayList<UserActivity>)
    }

    private fun setupUI() {
        setupToolbar()
        setupRecyclerView()
    }

    private fun setupToolbar() {
        binding.historyToolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        with(binding.historyRecyclerView) {
            adapter = historyAdapter
            layoutManager = LinearLayoutManager(this@UserHistoryActivity)
        }
    }
}