package com.shashank.mytodo.ui

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shashank.mytodo.R
import com.shashank.mytodo.data.ToDo
import com.shashank.mytodo.data.TodoViewModel
import kotlinx.android.synthetic.main.row_layout.view.*
import java.text.DateFormat

class TodoAdapter(private var mTodoViewModel: TodoViewModel) :
    RecyclerView.Adapter<TodoAdapter.MyViewHolder>() {

    private var todosList = emptyList<ToDo>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentTodo = todosList[position]

        val todoTitleTextView = holder.itemView.todoTitleTextView
        val todoDescriptionTextView = holder.itemView.todoDescriptionTextView

        todoTitleTextView.text = currentTodo.title.trim()
        todoDescriptionTextView.text = currentTodo.description.trim()

        holder.itemView.todoDateTextView.text =
            DateFormat.getDateTimeInstance().format(currentTodo.modifiedAt).toString()

        if (!currentTodo.completed) {
            removeStrikeThrough(todoTitleTextView, todoDescriptionTextView)
            holder.itemView.todoStatusImageView.setImageResource(R.drawable.ic_baseline_radio_button_unchecked_24)
        } else {
            applyStrikeThrough(todoTitleTextView, todoDescriptionTextView)
            holder.itemView.todoStatusImageView.setImageResource(R.drawable.ic_baseline_check_circle_24)
        }

        holder.itemView.todoStatusImageView.setOnClickListener {
            mTodoViewModel.updateTodo(
                ToDo(
                    currentTodo.id,
                    currentTodo.title,
                    currentTodo.description,
                    !currentTodo.completed,
                    currentTodo.createdAt,
                    currentTodo.modifiedAt,
                    currentTodo.completedAt
                )
            )
            if (!currentTodo.completed) {
                mTodoViewModel.deleteTodoInBackground(currentTodo.id)
                applyStrikeThrough(todoTitleTextView, todoDescriptionTextView)
            } else {
                mTodoViewModel.cancelTodoDeletion(currentTodo.id)
                removeStrikeThrough(todoTitleTextView, todoDescriptionTextView)
            }
        }

    }

    private fun applyStrikeThrough(
        todoTitleTextView: TextView,
        todoDescriptionTextView: TextView
    ) {
        todoTitleTextView.paintFlags =
            todoTitleTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        todoDescriptionTextView.paintFlags =
            todoDescriptionTextView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    }

    private fun removeStrikeThrough(
        todoTitleTextView: TextView,
        todoDescriptionTextView: TextView
    ) {
        todoTitleTextView.paintFlags =
            todoTitleTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        todoDescriptionTextView.paintFlags =
            todoDescriptionTextView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
    }

    override fun getItemCount(): Int {
        return todosList.size
    }

    fun getNote(position: Int): ToDo {
        return todosList[position]
    }

    fun setData(list: List<ToDo>) {
        todosList = list
        notifyDataSetChanged()
    }

}