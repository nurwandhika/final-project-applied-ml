package com.dicoding.asclepius.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.model.UserActivity

@Dao
interface UserHistoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNewUserHistory(userActivity: UserActivity): Long

    @Query(SELECT_ALL_FROM_HISTORY)
    fun fetchAllUserHistories(): LiveData<List<UserActivity>>

    companion object {
        const val HISTORY_TABLE_NAME = "user_activity"
        const val SELECT_ALL_FROM_HISTORY = "SELECT * FROM $HISTORY_TABLE_NAME"
    }
}