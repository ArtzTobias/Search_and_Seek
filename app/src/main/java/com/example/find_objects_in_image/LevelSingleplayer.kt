package com.example.find_objects_in_image

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.util.Size
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.find_objects_in_image.databinding.ActivityMainBinding
import com.example.find_objects_in_image.databinding.SinglePlayerBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Date
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.math.abs

class LevelSingleplayer: AppCompatActivity() {
    @OptIn(ExperimentalEncodingApi::class)

    private lateinit var imageLayout: LinearLayout
    private var imageViewHeight: Int = 0
    private var imageViewWidth: Int = 0

    private lateinit var textForLabel: TextView
    private var labelText: String = ""

    private var buttonCoordinates = listOf<Int>()
    private lateinit var buttonForObject: Button

    private var allObjectsNumberList = listOf<Int>()
    private lateinit var singleObject: JsonManager.ObjectClass
    private var amountOfObjects = 0
    private var indexObject = 0

    private var actualLevel = 0

    private lateinit var buttonWrong: Button
    private lateinit var buttonAvatar: Button
    private lateinit var textviewSpeech: TextView
    private lateinit var bottomLayout: LinearLayout

    private lateinit var allCategoryObjects: List<JsonManager.ObjectClass>
    private lateinit var category: String

    private lateinit var backButton: Button
    private lateinit var adViewContainer: FrameLayout

    private lateinit var drawableAvatar: Drawable

    private var quoteListNormal = listOf<String>(
        "Do you want to find my lost object?",
        "Oh my, I am searching for hours now but with no luck...",
        "Oh, you are here...search for the object.",
        "I cannot find the object, can you help me?",
        "Where is the object?")

    private var quoteListSolution = listOf<String>(
        "Fine, I will show you the object.",
        "Ah, I have found the object. The object is located at this position!",
        "See? The object is over there!")

    private var quoteListError = listOf<String>(
        "Hmm? I cannot see the object at this location.",
        "Where? You must be mistaken.",
        "Without a doubt, this is not my lost object.",
        "No.",
        "I cannot see it.",
        "Ehm...no.",
        "This is not the object we are looking at.",
        "Let me see...no.",
        "Keep looking!",
        "Found anything? No.",
        "Where?",
        "Try again!",
        "Maybe next time.",
        "No match."
    )

    private var quoteListNotFound = listOf<String>(
        "Well, I have found the object! I can tell you where it is!",
        "You cannot find it? If you need help, ask me!",
        "I can show you, where the object is!")

    private var quoteListStart = listOf<String>(
        "Hey, welcome to the level. You can click me if you need an advise.",
        "We will find the next objects! Click me and we can talk.",
        "Hello there, if you want to talk with me, click me.",
        "Lets go! Together, we can find the lost objects! Just click me, if you need anything.",
        "I have lost precious objects. Help me to find it. We can chat every time, just click me.")

    private var quoteListFindings = listOf<String>(
        "Excellent!",
        "Well done!",
        "Nice work!",
        "Nice!",
        "Good job!",
        "Yes!",
        "Hurray!",
        "Bingo!",
        "Good!",
        "Splendid!",
        "Superb!",
        "Magnificent!"
    )

    private var textIndex: Int = 0
    private val handler = Handler(Looper.getMainLooper())

    private var wrongCounter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.single_player)

        buttonWrong = findViewById(R.id.buttonWrong)
        buttonWrong.setBackgroundColor(Color.TRANSPARENT)

        buttonWrong.setOnClickListener { view ->
            buttonWrongFunction(view)
        }

        bottomLayout = findViewById(R.id.singleplayer_info_bottom_layout)
        buttonAvatar = findViewById(R.id.buttonAvatar)
        textviewSpeech = findViewById(R.id.textviewSpeech)

        // get size of bottomLayout
        // update Button Avatar
        bottomLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                // Remove the listener to prevent multiple calls
                bottomLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Update Button Location
                //updateButtonDependencies(bottomLayout.width, bottomLayout.height)

                Log.d("SIZE_Layout_Bottom", bottomLayout.width.toString())
                Log.d("SIZE_Layout2_Bottom2", bottomLayout.height.toString())

                // Quadratic Avatar Button
                buttonAvatar.width = bottomLayout.height
                buttonAvatar.height = bottomLayout.height
            }
        })


        imageLayout = findViewById<LinearLayout>(R.id.singleplayer_image_layout)
        textForLabel = findViewById<TextView>(R.id.label_of_button)
        buttonForObject = findViewById<Button>(R.id.buttonForObject)

        val categoryIntent = intent.extras
        category = categoryIntent?.getString("category").toString()

        if (category == "Living Room"){
            allCategoryObjects = getLivingRoomObjects()
            drawableAvatar = ContextCompat.getDrawable(this, R.drawable.avatar_final_mirrored)!!

        }
        if (category == "Bath Room"){
            allCategoryObjects = getBathRoomObjects()
            drawableAvatar = ContextCompat.getDrawable(this, R.drawable.avatar_final_2)!!

        }
        if (category == "Bedroom"){
            allCategoryObjects = getBedroomObjects()
            drawableAvatar = ContextCompat.getDrawable(this, R.drawable.avatar_final_3)!!

        }

        // Get Avatar
        buttonAvatar.foreground = drawableAvatar

        buttonAvatar.setOnClickListener { view ->
            buttonAvatarFunction(view)
        }

        val levelSelected = intent.extras
        actualLevel = levelSelected?.getInt("level") as Int

        singleObject = allCategoryObjects[actualLevel]

        amountOfObjects = singleObject.objects.size

        val listOfObjects = (0..<amountOfObjects).toMutableList()
        allObjectsNumberList = listOfObjects.shuffled()


        selectBackgroundImage(singleObject)

        selectObjectByIndex(singleObject, allObjectsNumberList[indexObject])

        // Init random start quote
        textviewSpeech.text = ""
        speakLikeYakuza(quoteListStart)

        backButton = findViewById(R.id.button_back_ingame)
        backButton.setOnClickListener {
            // Handler code here.
            val intent = Intent(this, AllLevelsSingleplayer::class.java)
            intent.putExtra("category", category)
            startActivity(intent);
        }

        // Add google ads
        val backgroundScope = CoroutineScope(Dispatchers.IO)
        backgroundScope.launch {
            // Initialize the Google Mobile Ads SDK on a background thread.
            MobileAds.initialize(this@LevelSingleplayer) {}
        }

        adViewContainer = findViewById(R.id.adViewContainer)

        // Load the banner ad
        loadBanner()

    }

    fun getLivingRoomObjects(): List<JsonManager.ObjectClass> {
        val jsonManager = JsonManager()

        val allObjectsLivingRoom = jsonManager.getLivingRoomObjects(this)

        return allObjectsLivingRoom
    }

    fun getBathRoomObjects(): List<JsonManager.ObjectClass> {
        val jsonManager = JsonManager()

        val allObjectsBathRoom = jsonManager.getBathRoomObjects(this)

        return allObjectsBathRoom
    }

    fun getBedroomObjects(): List<JsonManager.ObjectClass> {
        val jsonManager = JsonManager()

        val allObjectsBedroom = jsonManager.getBedroomObjects(this)

        return allObjectsBedroom
    }

    @OptIn(ExperimentalEncodingApi::class)
    fun getDecodedImage(base64String: String):Bitmap{
        val imageBytes = Base64.Default.decode(base64String)
        val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

        return decodedImage
    }

    fun buttonClickFunction(view: View){
        wrongCounter = 0
        textviewSpeech.text = ""
        speakLikeYakuza(quoteListFindings)
        deactivateButtons()
        with (view as Button){

            indexObject +=1
            if (indexObject == (amountOfObjects)){
                showPopupWindow()
            } else {
                changeButtonLocationToNewObject(allObjectsNumberList[indexObject], imageViewWidth, imageViewHeight)
            }
        }
        activateButtons()
    }

    fun updateButtonDependencies(width: Int, height: Int){
        imageViewWidth = width
        imageViewHeight = height

        Log.d("MyValue1", imageViewWidth.toString())
        Log.d("MyValue2", imageViewHeight.toString())

        //updateTextView()
        updateButtonPosition()
    }

    private fun updateTextView(){
        val text = "\n Width: $imageViewWidth, \n Height: $imageViewHeight"
        textForLabel.text = text
    }

    private fun updateButtonPosition(){

        val heightDependency = (imageViewHeight).toFloat()/(512).toFloat()
        val widthDependency = (imageViewWidth).toFloat()/(512).toFloat()
        //textForLabel.text = buttonLabel
        val left = (buttonCoordinates[0] * widthDependency).toInt()
        val top = (buttonCoordinates[1] * heightDependency).toInt()
        val right = (buttonCoordinates[2] * widthDependency).toInt()
        val bottom = (buttonCoordinates[3] * heightDependency).toInt()

        buttonForObject.setOnClickListener { view ->
            buttonClickFunction(view)
        }

        val text = labelText
        textForLabel.text = text

        //set button position
        val params = buttonForObject.layoutParams as RelativeLayout.LayoutParams
        params.leftMargin = left
        params.topMargin = top
        params.width = abs(left - right)
        params.height = abs(top - bottom)

        buttonForObject.layoutParams = params

        buttonForObject.setBackgroundColor(Color.TRANSPARENT)
        buttonForObject.foreground = ContextCompat.getDrawable(this, R.drawable.transparent)
    }

    private fun selectBackgroundImage(singleObject: JsonManager.ObjectClass){
        val encodedImage = singleObject.image_encoded

        // decoded image
        val image = getDecodedImage(encodedImage)

        // add image
        val drawableImage = BitmapDrawable(resources, image)
        //imageView.setImageBitmap(image)
        imageLayout.background = drawableImage
    }

    private fun selectObjectByIndex(singleObject: JsonManager.ObjectClass, index: Int){

        val labelsAndBoxes = singleObject.objects
        buttonCoordinates = labelsAndBoxes[index].box
        labelText = labelsAndBoxes[index].label

        // get size of imageLayout
        // update Button Location
        imageLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener{
            override fun onGlobalLayout() {
                // Remove the listener to prevent multiple calls
                imageLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                // Update Button Location
                changeButtonLocationToNewObject(index, imageLayout.width, imageLayout.height)

                Log.d("SIZE_Layout", imageLayout.width.toString())
                Log.d("SIZE_Layout2", imageLayout.height.toString())

                imageViewWidth = imageLayout.width
                imageViewHeight = imageLayout.height
            }
        })
    }

    fun changeButtonLocationToNewObject(index: Int, imageLayoutWidth: Int, imageLayoutHeight: Int){
        val labelsAndBoxes = singleObject.objects
        buttonCoordinates = labelsAndBoxes[index].box
        labelText = labelsAndBoxes[index].label

        updateButtonDependencies(imageLayoutWidth, imageLayoutHeight)
    }
    private fun showPopupWindow() {
        val intent = Intent(this, WinPopUp::class.java)
        intent.putExtra("level", actualLevel)
        intent.putExtra("category", category)
        startActivity(intent);
    }

    private fun buttonAvatarFunction(view: View){
        talk()
    }

    // Blink one time for a hint
    private fun showSearchedButton(){
        buttonForObject.setBackgroundResource(R.drawable.button_blink_animation)
        val animationBlink = buttonForObject.background as AnimationDrawable
        animationBlink.start()

    }

    private fun talk(){
        textviewSpeech.text = ""


        if (wrongCounter >= 3){
            speakLikeYakuza(quoteListSolution)
            showSearchedButton()
        } else {
            speakLikeYakuza(quoteListNormal)
        }


    }

    private fun speakLikeYakuza(listQuotes: List<String>) {
        deactivateButtons()
        var randomQuote = listQuotes.random()
        val maxLetters = randomQuote.chars().toArray().size
        var letterCounter = 0
        var isRunning = true
        val updateTextTaskStart = object : Runnable {
            override fun run() {

                if (letterCounter < maxLetters) {
                    val letter = randomQuote[letterCounter]
                    val currentText = textviewSpeech.text

                    val nextText = "$currentText$letter"

                    textviewSpeech.text = nextText
                    letterCounter++
                    handler.postDelayed(this, 20)
                    //waitMilliSeconds(10)
                } else {
                    activateButtons()
                    handler.removeCallbacks(this)
                }
            }
        }
        handler.post(updateTextTaskStart)
    }

    private fun waitMilliSeconds(milliseconds: Int){

        val timeGoal = Calendar.getInstance()

        timeGoal.add(Calendar.MILLISECOND, milliseconds)

        while (true){
            val currentTime = Calendar.getInstance()

            if (currentTime.time > timeGoal.time){
                return
            }

        }
    }

    private fun buttonWrongFunction(view: View){
        wrongCounter++
        textviewSpeech.text = ""

        if (wrongCounter % 3 == 0){
            speakLikeYakuza(quoteListNotFound)
        }else{
            speakLikeYakuza(quoteListError)
        }

    }

    private fun deactivateButtons(){
        buttonWrong.isEnabled = false
        buttonAvatar.isEnabled = false
        buttonForObject.isEnabled = false
    }

    private fun activateButtons(){
        buttonWrong.isEnabled = true
        buttonAvatar.isEnabled = true
        buttonForObject.isEnabled = true
    }


    // Determine the screen width (less decorations) to use for the ad width.
    // If the ad hasn't been laid out, default to the full screen width.
    private fun getAdSize(): AdSize {
        val display = windowManager.defaultDisplay
        val outMetrics = DisplayMetrics()
        display.getMetrics(outMetrics)

        val density = outMetrics.density

        var adWidthPixels = adViewContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
    }

    private fun loadBanner() {

        // Create a new ad view.
        val adView = findViewById<AdView>(R.id.adView)

        //adViewContainer.addView(adView)

        //Calculate adSize
        val adSize = getAdSize()
        Log.d("ADSIZE", adSize.toString())

        //adView.setAdSize(adSize)

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        // Start loading the ad in the background.
        adView.loadAd(adRequest)

        // Set an AdListener to handle potential issues
        adView.adListener = object : AdListener() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Log the error
                Log.e("AdView", "Ad failed to load: ${adError.message}")
            }

            override fun onAdLoaded() {
                // Ad loaded successfully
                Log.d("AdView", "Ad loaded successfully")
            }
        }
    }
}