package com.shashank.mytodo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.shashank.mytodo.R
import com.shashank.mytodo.data.ToDo
import com.shashank.mytodo.data.TodoViewModel
import kotlinx.android.synthetic.main.fragment_add_todo.*

class AddTodoFragment : Fragment() {

    private lateinit var mTodoViewModel: TodoViewModel
    private val args by navArgs<AddTodoFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_todo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTodoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        // UPDATE FRAGMENT'S TITLE
        if (args.todo != null) {
            addTodoTopAppBar.title = "Edit TODO"
            todoTitleTextInputEditText.setText(args.todo!!.title)
            todoDescriptionTextInputEditText.setText(args.todo!!.description)
        }

        // TO NAVIGATE BACK
        addTodoTopAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        // ITEM LISTENER FOR MENU (SAVE)
        addTodoTopAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.saveToDo -> {
                    if (args.todo != null) updateTodo()
                    else addTodo()
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Function to Update the TODO_Item
     */
    private fun updateTodo() {
        val title = todoTitleTextInputEditText.text.toString()
        val desc = todoDescriptionTextInputEditText.text.toString()
        val newTodo = ToDo(
            args.todo!!.id,
            title.trim(),
            desc.trim(),
            args.todo!!.completed,
            System.currentTimeMillis(),
            args.todo!!.completedAt
        )
        mTodoViewModel.updateTodo(newTodo)
        requireActivity().onBackPressed()
    }

    /**
     * Function to Add new TODO_Item.
     */
    private fun addTodo() {
        val title = todoTitleTextInputEditText.text.toString()
        val desc = todoDescriptionTextInputEditText.text.toString()
        val newTodo = ToDo(0, title.trim(), desc.trim(), false, System.currentTimeMillis())
        mTodoViewModel.addTodo(newTodo)
        requireActivity().onBackPressed()
    }

}