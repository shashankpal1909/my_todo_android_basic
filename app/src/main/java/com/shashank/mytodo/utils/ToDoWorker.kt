package com.shashank.mytodo.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.shashank.mytodo.data.TodoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ToDoWorker(
    ctx: Context,
    params: WorkerParameters,
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {

        val todoID = inputData.getInt("ARCHIVED_TODO_ID", 0)
        Log.wtf("WORKER", "WORKER ACTIVE")

        withContext(Dispatchers.IO) {
            val todoDao = TodoDatabase.getDatabase(applicationContext).todoDao()
            val tobeDeleted = todoDao.getTodoByID(todoID)
            todoDao.deleteTodo(tobeDeleted)
            todoDao.deleteTodo(tobeDeleted)
        }

        return Result.success()

    }

}