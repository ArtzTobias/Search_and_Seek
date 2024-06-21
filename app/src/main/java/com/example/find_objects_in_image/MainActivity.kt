package com.example.find_objects_in_image

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val context = this

        // Singleplayer
        val playButton = findViewById<Button>(R.id.play_button)
        playButton.setOnClickListener {
            // Handler code here.
            val intent = Intent(context, CategorySelection::class.java)
            startActivity(intent);
        }

        //val sharedPreferencesManager = SharedPreferencesManager()
        //sharedPreferencesManager.resetProgress(this)

    }

    fun addAllContainingLivingRoomClasses(intent: Intent, allObjectsLivingRoom: List<JsonManager.ObjectClass>): Intent{
        val idCounter = 0
        for(livingRoomObject in allObjectsLivingRoom){
            intent.putExtra(idCounter.toString(), livingRoomObject)
            break
        }

        return intent
    }

}