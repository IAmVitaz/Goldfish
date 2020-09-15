package com.vitaz.goldfish.fragments

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.vitaz.goldfish.data.models.Priority

class SharedViewModel(application: Application): AndroidViewModel(application) {

    fun verifyDataFromUser(title: String, description: String): Boolean {
        return !(title.isEmpty() || description.isEmpty())
    }

    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High Priority" -> {
                Priority.HIGH}
            "Medium Priority" -> {
                Priority.MEDIUM}
            "Low Priority" -> {
                Priority.LOW}
            else -> Priority.LOW
        }
    }

}