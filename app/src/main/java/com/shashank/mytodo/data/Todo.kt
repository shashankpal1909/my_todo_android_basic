package com.shashank.mytodo.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "todo_table")
data class ToDo(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    val completed: Boolean = false,
    val createdAt: Long,
    val modifiedAt: Long = System.currentTimeMillis(),
    val completedAt: Long = -1
) : Parcelable
