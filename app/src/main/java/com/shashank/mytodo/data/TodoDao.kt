package com.shashank.mytodo.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTodo(todo: ToDo)

    @Update
    suspend fun updateTodo(todo: ToDo)

    @Delete
    suspend fun deleteTodo(todo: ToDo)

    @Query("SELECT * FROM todo_table ORDER BY createdAt DESC")
    fun getTodos(): LiveData<List<ToDo>>

    @Query("SELECT * FROM TODO_TABLE WHERE id = :id")
    fun getTodoByID(id: Int): ToDo

    @Query("SELECT * FROM todo_table WHERE title LIKE :query OR description LIKE :query ORDER BY createdAt DESC")
    fun getMatchingTodos(query: String): LiveData<List<ToDo>>

}