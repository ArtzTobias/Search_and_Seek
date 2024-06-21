package com.example.find_objects_in_image

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WinPopUp: AppCompatActivity() {

    private var newLevel = 0
    private var lastLevel = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pop_up_win)

        val allLevelsButton = findViewById<Button>(R.id.button_levels)
        val nextLevelButton = findViewById<Button>(R.id.button_next_level)

        val currentIntent = intent.extras
        val currentLevelNumber = currentIntent?.getInt("level")
        val category = currentIntent?.getString("category")

        if (currentLevelNumber == 100){
            lastLevel = true
            newLevel = currentLevelNumber
        } else {
            if (currentLevelNumber != null) {
                newLevel = currentLevelNumber + 1
            }
        }

        // update levels: clear current level, unlock next level
        val sharedPreferencesManager = SharedPreferencesManager()
        if (category != null && currentLevelNumber != null) {
            sharedPreferencesManager.updatePreferencesLevelCleared(this, category, currentLevelNumber)
            sharedPreferencesManager.updatePreferencesLevelUnlocked(this, category, newLevel)
        }

        allLevelsButton.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, AllLevelsSingleplayer::class.java)
            intent.putExtra("category", category)
            startActivity(intent);
        }

        nextLevelButton.setOnClickListener {


            if (lastLevel == true) {
                val intent = Intent(this, AllLevelsSingleplayer::class.java)
                intent.putExtra("category", category)
                startActivity(intent);
            } else {
                val intent = Intent(this, LevelSingleplayer::class.java)
                intent.putExtra("level", newLevel)
                intent.putExtra("category", category)
                startActivity(intent);
            }
        }
    }
}