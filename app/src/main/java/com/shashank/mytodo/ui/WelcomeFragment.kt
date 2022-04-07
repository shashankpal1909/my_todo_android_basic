package com.shashank.mytodo.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.shashank.mytodo.R
import kotlinx.android.synthetic.main.fragment_welcome.*

class WelcomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        yourNameTextInputEditText.doAfterTextChanged {
            yourNameTextInputLayout.error = null
            yourNameTextInputLayout.isErrorEnabled = false
        }

        userNameTextInputEditText.doAfterTextChanged {
            userNameTextInputLayout.error = null
            userNameTextInputLayout.isErrorEnabled = false
        }

        startButton.setOnClickListener {
            val name = yourNameTextInputEditText.text.toString()
            val userName = userNameTextInputEditText.text.toString()

            when {
                name == "" -> yourNameTextInputLayout.error = "Enter a name!"
                userName == "" -> userNameTextInputLayout.error = "Enter a username!"
                else -> {
                    val sharedPref =
                        requireActivity().getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE)
                    with(sharedPref.edit()) {
                        putString("NAME", name)
                        putString("USER_NAME", userName)
                    }.apply()
                    findNavController().navigate(
                        WelcomeFragmentDirections.actionWelcomeFragmentToTodoListFragment()
                    )
                }
            }
        }

    }
}