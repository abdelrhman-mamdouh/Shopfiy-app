package com.example.exclusive.utilities

import android.content.Context
import android.content.SharedPreferences
import com.example.exclusive.data.model.PriceRuleSummary
import com.google.gson.Gson

object SharedPreferencesManager {

    private const val PREF_NAME = "MyPrefs"
    private const val KEY_PRICE_RULE = "priceRule"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun savePriceRule(context: Context, priceRuleSummary: PriceRuleSummary) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val priceRuleJson = gson.toJson(priceRuleSummary)
        editor.putString(KEY_PRICE_RULE, priceRuleJson)
        editor.apply()
    }

    fun getPriceRule(context: Context): PriceRuleSummary? {
        val sharedPreferences = getSharedPreferences(context)
        val gson = Gson()
        val priceRuleJson = sharedPreferences.getString(KEY_PRICE_RULE, null)
        return gson.fromJson(priceRuleJson, PriceRuleSummary::class.java)
    }
}
