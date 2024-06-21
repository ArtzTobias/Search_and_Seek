package com.example.find_objects_in_image

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toDrawable

class CategorySelection: AppCompatActivity() {

    private lateinit var button_living_room: Button
    private lateinit var button_bath_room: Button
    private lateinit var button_bedroom: Button
    private lateinit var button_back: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.category_selection)

        button_living_room = findViewById(R.id.button_living_room_category)
        button_living_room.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, AllLevelsSingleplayer::class.java)
            intent.putExtra("category", "Living Room")
            startActivity(intent);
        }

        button_bath_room = findViewById(R.id.button_bathroom_category)
        button_bath_room.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, AllLevelsSingleplayer::class.java)
            intent.putExtra("category", "Bath Room")
            startActivity(intent);
        }

        button_bedroom = findViewById(R.id.button_bedroom_category)
        button_bedroom.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, AllLevelsSingleplayer::class.java)
            intent.putExtra("category", "Bedroom")
            startActivity(intent);
        }

        button_back = findViewById(R.id.button_back_categories)
        button_back.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent);
        }
    }
}