package com.shashank.mytodo.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.shashank.mytodo.utils.ToDoWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class TodoViewModel(application: Application) : AndroidViewModel(application) {

    var getTodos: LiveData<List<ToDo>>

    private val repository: TodoRepository
    private val workManager: WorkManager
    private val sharedPref = application.getSharedPreferences("DELETION_TIME", Context.MODE_PRIVATE)

    init {
        val noteDao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(noteDao)
        getTodos = repository.getTodos
        workManager = WorkManager.getInstance(application)
    }

    fun deleteTodoInBackground(id: Int) {
        var time = 1
        if (sharedPref != null) {
            time = sharedPref.getInt("TIME", 1200)
        }
        val myData = workDataOf("ARCHIVED_TODO_ID" to id)
        val work = OneTimeWorkRequestBuilder<ToDoWorker>()
            .setInitialDelay(time.toLong(), TimeUnit.SECONDS)
            .setInputData(myData)
            .build()
        workManager
            .beginUniqueWork(id.toString(), ExistingWorkPolicy.REPLACE, work)
            .enqueue()
    }

    fun cancelTodoDeletion(id: Int) {
        workManager.cancelUniqueWork(id.toString())
    }

    fun addTodo(todo: ToDo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todo)
        }
    }

    fun updateTodo(todo: ToDo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: ToDo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(todo)
        }
    }

    fun getMatchingTodos(query: String): LiveData<List<ToDo>> {
        return repository.getMatchingTodos(query)
    }

}