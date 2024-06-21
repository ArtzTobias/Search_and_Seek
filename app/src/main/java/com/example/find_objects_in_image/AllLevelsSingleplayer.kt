package com.example.find_objects_in_image

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.marginLeft
import androidx.core.view.marginStart

class AllLevelsSingleplayer: AppCompatActivity() {

    private lateinit var linearLayoutForEntireLayout: LinearLayout
    private var amountOfLevels = 100
    private lateinit var linearLayoutRow: LinearLayout
    private lateinit var category: String
    private lateinit var layout_entire: LinearLayout
    private lateinit var button_back: Button
    private lateinit var textviewCaption: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.level_singleplayer)

        linearLayoutForEntireLayout = findViewById(R.id.entireLayout)

        val sharedPreferencesManager = SharedPreferencesManager()
        val categoryIntent = intent.extras
        if (categoryIntent != null) {
            category = categoryIntent.getString("category").toString()
        }

        layout_entire = findViewById(R.id.layout_Entire_page_levelSelection)

        // Set Background
        if (category == "Living Room"){
            layout_entire.background = ContextCompat.getDrawable(this, R.drawable.living_room_levels)
        }
        if (category == "Bath Room"){
            layout_entire.background = ContextCompat.getDrawable(this, R.drawable.bath_room_levels)
        }
        if (category == "Bedroom"){
            layout_entire.background = ContextCompat.getDrawable(this, R.drawable.bedroom_levels)
        }

        // Set caption
        textviewCaption = findViewById(R.id.text_level_selection)
        val caption = "$category Levels"
        textviewCaption.text = caption

        // Set Back Button
        button_back = findViewById(R.id.button_back_selection_level)
        button_back.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, CategorySelection::class.java)
            startActivity(intent);
        }

        // unlock first level
        sharedPreferencesManager.updatePreferencesLevelUnlocked(this, category, 0)

        val allLevelList = (0..amountOfLevels).toMutableList()

        for (level in allLevelList){

            // create new Row
            if (level % 5 == 0){

                // Add row to Entire Layout
                if (level >= 5){
                    linearLayoutForEntireLayout.addView(linearLayoutRow)
                }

                // create new Row
                linearLayoutRow = LinearLayout(this)
                linearLayoutRow.orientation = LinearLayout.HORIZONTAL
                linearLayoutRow.weightSum = 5f
            }

            // Create Level Button
            val levelButton = AppCompatButton(this)
            val displayLevel = level + 1
            levelButton.text = displayLevel.toString()
            levelButton.textSize = 25f
            levelButton.setTextColor(Color.WHITE)
            levelButton.setBackgroundResource(R.drawable.rounded_button)

            // check if unlocked
            val isUnlockedLevel = sharedPreferencesManager.loadPreferencesLevelUnlocked(this, category, level)

            if (isUnlockedLevel == false){
                levelButton.foreground = ContextCompat.getDrawable(this, R.drawable.lock_final)
                levelButton.isEnabled = false
            }

            // check if cleared
            val isClearedLevel = sharedPreferencesManager.loadPreferencesLevelCleared(this, category, level)

            if (isClearedLevel == true){
                levelButton.foreground = ContextCompat.getDrawable(this, R.drawable.cleared_final)
            }

            levelButton.setOnClickListener {
                // Handler code here.
                val intent = Intent(this, LevelSingleplayer::class.java)
                intent.putExtra("level", level)
                intent.putExtra("category", category)
                startActivity(intent);
            }

            // Create Button parameters
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.weight = 1f
            params.marginStart = 15 // Set margin in pixels
            params.marginEnd = 15 // Set margin in pixels
            params.topMargin = 15
            params.bottomMargin = 15
            levelButton.layoutParams = params

            // Change button Color
            if (category == "Living Room"){
                val newColor = Color.rgb(23, 115, 2)
                val background = levelButton.background as GradientDrawable
                background.setColor(newColor)
            }

            if (category == "Bath Room"){
                val newColor = Color.rgb(5, 76, 255)
                val background = levelButton.background as GradientDrawable
                background.setColor(newColor)
            }

            if (category == "Bedroom"){
                val newColor = Color.rgb(255, 193, 7)
                val background = levelButton.background as GradientDrawable
                background.setColor(newColor)
            }

            // Add Button to Row
            linearLayoutRow.addView(levelButton)

        }
    }
}