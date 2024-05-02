package com.dicoding.asclepius.data.remote.repository

import com.dicoding.asclepius.data.local.dao.UserHistoryDao
import com.dicoding.asclepius.data.model.UserActivity
import java.util.concurrent.Executors

class UserActivityRepository(
    private val userActivityDataSource: UserHistoryDao
) {
    fun addActivityEntry(userActivity: UserActivity) {
        executeNewUserHistory(userActivity)
    }

    private fun executeNewUserHistory(userActivity: UserActivity) {
        Executors.newSingleThreadExecutor()
            .execute { userActivityDataSource.addNewUserHistory(userActivity) }
    }

    fun fetchAllActivityEntries() = userActivityDataSource.fetchAllUserHistories()
}