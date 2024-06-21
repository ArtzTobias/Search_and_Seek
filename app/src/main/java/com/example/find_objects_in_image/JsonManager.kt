package com.example.find_objects_in_image

import android.content.Context
import android.icu.text.ListFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.Serializable
import java.lang.reflect.Type


class JsonManager {

    val gson = Gson()
    val json_livingroom = "livingroom.json"
    val json_bathroom = "bathroom.json"
    val json_bedroom = "bedroom.json"

    data class SingleObject(
        var label: String,
        var box: List<Int>
    ): Serializable

    data class ObjectClass(
        var name: String,
        var image_encoded: String,
        var objects: List<SingleObject>
    ): Serializable

    data class Category(
        var data: List<ObjectClass>
    )

    private fun openJson(context: Context, fileName: String): Category {

        val jsonString: String = context.assets.open(fileName).bufferedReader().use{
            it.readText()
        }
        val singleObject = gson.fromJson(jsonString, Category::class.java)

        return singleObject
    }


    private fun getLivingRoomData(context: Context): Category {
        val singleObject = openJson(context,json_livingroom)

        return singleObject
    }

    private fun getBathRoomData(context: Context): Category {
        val singleObject = openJson(context, json_bathroom)

        return singleObject
    }

    private fun getBedroomData(context: Context): Category {
        val singleObject = openJson(context, json_bedroom)

        return singleObject
    }

    private fun geAllObjectsFromCategory(context: Context, category: Category): List<ObjectClass> {
        val all_objects = category.data

        return all_objects
    }

    fun getLivingRoomObjects(context: Context): List<ObjectClass>{

        val livingRoomData = getLivingRoomData(context)
        val livingRoomObjects = geAllObjectsFromCategory(context, livingRoomData)

        return livingRoomObjects
    }

    fun getBathRoomObjects(context: Context): List<ObjectClass>{

        val bathRoomData = getBathRoomData(context)
        val bathRoomObjects = geAllObjectsFromCategory(context, bathRoomData)

        return bathRoomObjects
    }

    fun getBedroomObjects(context: Context): List<ObjectClass>{
        val bedRoomData = getBedroomData(context)
        val bedRoomObjects = geAllObjectsFromCategory(context, bedRoomData)

        return bedRoomObjects
    }
}
