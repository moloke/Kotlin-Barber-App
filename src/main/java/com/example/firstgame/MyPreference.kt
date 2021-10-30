package com.example.firstgame

import android.content.Context

class MyPreference (context : Context) {

    val PREFERENCE_NAME = "SharedPreferences"

    val PREFERENCE_NUMBER = "num"
    val PREFERENCE_POSTCODE = "post"
    val PREFERENCE_DESCRIPTION = "des"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getNumber() : String{
        return preference.getString(PREFERENCE_NUMBER, "0795095mayo")

    }

    fun setNumber(number: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_NUMBER, number)
        editor.apply()
    }

    fun getPostcode() : String{
        return preference.getString(PREFERENCE_POSTCODE, "Testing postcode")

    }

    fun setPostcode(postcode: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_POSTCODE, postcode)
        editor.apply()
    }

    fun getDes() : String{
        return preference.getString(PREFERENCE_DESCRIPTION, "Write a bit about yourself, services you offer, discounts and any terms and conditions")

    }


    fun setDes(description: String){
        val editor = preference.edit()
        editor.putString(PREFERENCE_DESCRIPTION, description)
        editor.apply()
    }

}