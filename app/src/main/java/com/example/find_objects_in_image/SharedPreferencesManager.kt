package com.example.find_objects_in_image

import android.app.Activity
import android.content.Context

class SharedPreferencesManager {


    // Unlocked Load
    fun loadPreferencesLevelUnlocked(context: Context, category: String, level: Int): Boolean{
        val sharedPreferences = context.getSharedPreferences(category, Context.MODE_PRIVATE)
        val key = "level_unlocked_$level"
        val levelunlocked = sharedPreferences.getBoolean(key, false)

        return levelunlocked
    }

    // Unlocked Update
    fun updatePreferencesLevelUnlocked(context: Context, category: String, level: Int){
        val sharedPreferences = context.getSharedPreferences(category, Context.MODE_PRIVATE)
        val key = "level_unlocked_$level"
        with (sharedPreferences.edit()) {
            putBoolean(key, true)
            apply()
        }
    }

    // Clear Load
    fun loadPreferencesLevelCleared(context: Context, category: String, level: Int): Boolean{
        val sharedPreferences = context.getSharedPreferences(category, Context.MODE_PRIVATE)
        val key = "level_cleared_$level"
        val levelclear = sharedPreferences.getBoolean(key, false)

        return levelclear
    }

    // Clear Update
    fun updatePreferencesLevelCleared(context: Context, category: String, level: Int){
        val sharedPreferences = context.getSharedPreferences(category, Context.MODE_PRIVATE)
        val key = "level_cleared_$level"
        with (sharedPreferences.edit()) {
            putBoolean(key, true)
            apply()
        }
    }

    fun resetProgress(context: Context) {
        val allCategories = listOf<String>("Living Room", "Bath Room", "Bedroom")
        val allLevels = (0..100).toList()

        for (category in allCategories){
            for (level in allLevels){
                updatePreferencesLevelClearedReset(context, category, level)
                updatePreferencesLevelUnlockedReset(context, category, level)
            }
        }
    }

    // Clear Reset
    private fun updatePreferencesLevelClearedReset(context: Context, category: String, level: Int){
        val sharedPreferences = context.getSharedPreferences(category, Context.MODE_PRIVATE)
        val key = "level_cleared_$level"
        with (sharedPreferences.edit()) {
            putBoolean(key, false)
            apply()
        }
    }
    // Unlocked Reset
    private fun updatePreferencesLevelUnlockedReset(context: Context, category: String, level: Int) {
        val sharedPreferences = context.getSharedPreferences(category, Context.MODE_PRIVATE)
        val key = "level_unlocked_$level"
        with(sharedPreferences.edit()) {
            putBoolean(key, false)
            apply()
        }
    }
}