package com.dicoding.asclepius.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_activity")
data class UserActivity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "activityImageUri")
    val activityImageUri: String,

    @ColumnInfo(name = "activityResults")
    val activityResults: String,
)
