package com.shashank.mytodo.data

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {

    var getTodos = todoDao.getTodos()

    fun getMatchingTodos(query: String): LiveData<List<ToDo>> {
        return todoDao.getMatchingTodos(query)
    }

    suspend fun addTodo(todo: ToDo) {
        todoDao.addTodo(todo)
    }

    suspend fun updateTodo(todo: ToDo) {
        todoDao.updateTodo(todo)
    }

    suspend fun deleteTodo(todo: ToDo) {
        todoDao.deleteTodo(todo)
    }

}