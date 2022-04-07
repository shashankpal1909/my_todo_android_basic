package com.shashank.mytodo.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.shashank.mytodo.R
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        val sharedPrefTheme = requireActivity().getSharedPreferences("THEME", Context.MODE_PRIVATE)

        if (sharedPrefTheme != null) {
            if (sharedPrefTheme.getString("MODE", null) == "dark") {
                view.darkThemeRadioButton.isChecked = true
            } else if (sharedPrefTheme.getString("MODE", null) == "light") {
                view.lightThemeRadioButton.isChecked = true
            }
        } else {
            with(sharedPrefTheme?.edit()) {
                this?.putString("MODE", "light")?.apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val sharedPrefTodoDeletionTime =
            requireActivity().getSharedPreferences("DELETION_TIME", Context.MODE_PRIVATE)

        if (sharedPrefTodoDeletionTime != null) {
            var time = sharedPrefTodoDeletionTime.getInt("TIME", -60) / 60
            if (time == -1) {
                time = 20
                sharedPrefTodoDeletionTime.edit().putInt("TIME", 1200).apply()
            }
            Log.wtf("LOG", time.toString())
            updateTimeStatusTextView(time)
            todoDeletionTimeSlider.value = time.toFloat()
        }

        view.lightThemeRadioButton.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            with(sharedPrefTheme?.edit()) {
                this?.putString("MODE", "light")?.apply()
            }
        }

        view.darkThemeRadioButton.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            with(sharedPrefTheme?.edit()) {
                this?.putString("MODE", "dark")?.apply()
            }
        }

        view.settingsFragmentTopAppBar.setNavigationOnClickListener {
            requireActivity().onBackPressed()
        }

        todoDeletionTimeSlider.addOnChangeListener { _, value, _ ->
            updateTimeStatusTextView(value.toInt())
            updateTodoDeleteTime(value.toInt())
        }

    }

    private fun updateTimeStatusTextView(time: Int) {
        if (time == 60)
            timeStatusTextView.text = getString(R.string.hour_1)
        else
            timeStatusTextView.text = getString(R.string.min, time)
    }

    private fun updateTodoDeleteTime(time: Int) {
        val sharedPref =
            requireActivity().getSharedPreferences("DELETION_TIME", Context.MODE_PRIVATE)
        val timeInSeconds = time * 60
        with(sharedPref.edit()) {
            this.putInt("TIME", timeInSeconds).apply()
        }
        Log.wtf("UPDATED", timeInSeconds.toString())
    }

}