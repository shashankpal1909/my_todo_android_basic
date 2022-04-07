package com.shashank.mytodo.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shashank.mytodo.R
import com.shashank.mytodo.data.ToDo
import com.shashank.mytodo.data.TodoViewModel
import com.shashank.mytodo.utils.SwipeGesture
import kotlinx.android.synthetic.main.fragment_todo_list.*

class TodoListFragment : Fragment() {

    private lateinit var mTodoViewModel: TodoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref =
            requireActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
        if (sharedPref.getString("NAME", null) == null)
            findNavController().navigate(
                TodoListFragmentDirections.actionTodoListFragmentToWelcomeFragment()
            )
        Log.wtf("SHARED", "onCreate: ${sharedPref.all}")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_todo_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mTodoViewModel = ViewModelProvider(this)[TodoViewModel::class.java]

        val myAdapter = TodoAdapter(mTodoViewModel)
        todoListRecyclerView.adapter = myAdapter
        todoListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val swipeGesture = object : SwipeGesture(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        findNavController().navigate(
                            TodoListFragmentDirections.actionTodoListFragmentToAddTodoFragment(
                                myAdapter.getNote(viewHolder.adapterPosition)
                            )
                        )
                    }
                    ItemTouchHelper.RIGHT -> {
                        Toast.makeText(context, "TODO Deleted", Toast.LENGTH_SHORT).show()
                        mTodoViewModel.deleteTodo(
                            myAdapter.getNote(viewHolder.adapterPosition)
                        )
                    }
                }
            }
        }

        val touchHelper = ItemTouchHelper(swipeGesture)
        touchHelper.attachToRecyclerView(todoListRecyclerView)

        todoListRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                super.onScrolled(recyclerView, dx, dy)
                // If the recycler view is scrolled above hide the FAB
                if (dy > 10 && addToDoFloatingActionButton.isShown) {
                    addToDoFloatingActionButton.hide()
                }
                // If the recycler view is scrolled above show the FAB
                if (dy < -10 && !addToDoFloatingActionButton.isShown) {
                    addToDoFloatingActionButton.show()
                }
                // If the recycler view is at the first item always show the FAB
                if (!recyclerView.canScrollVertically(-1)) {
                    addToDoFloatingActionButton.show()
                }

            }

        })

        mTodoViewModel.getTodos.observe(viewLifecycleOwner) {
            setEmptyTodoListStatus(it)
            myAdapter.setData(it)
        }

        searchTextInputEditText.doOnTextChanged { _, _, _, _ ->
            val query = "%" + searchTextInputEditText.text.toString() + "%"
            mTodoViewModel.getMatchingTodos(query).observe(viewLifecycleOwner) {
                setEmptyTodoListStatus(it)
                myAdapter.setData(it)
            }
        }

        addToDoFloatingActionButton.setOnClickListener {
            findNavController().navigate(
                TodoListFragmentDirections.actionTodoListFragmentToAddTodoFragment()
            )
        }

        todoListTopAppBar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.addTodo -> {
                    findNavController().navigate(
                        TodoListFragmentDirections.actionTodoListFragmentToAddTodoFragment()
                    )
                    true
                }
                R.id.settings -> {
                    findNavController().navigate(
                        TodoListFragmentDirections.actionTodoListFragmentToSettingsFragment()
                    )
                    true
                }
                else -> false
            }
        }

    }

    private fun setEmptyTodoListStatus(it: List<ToDo>) {
        if (it.isEmpty()) {
            emptyTodoLabelTextView.visibility = View.VISIBLE
        } else {
            emptyTodoLabelTextView.visibility = View.INVISIBLE
        }
    }

}