package com.currency.currencyconversionapi

import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class MainActivityViewModel :ViewModel(){
    var baseCurrency = "EUR"
    var convertedToCurrency = "USD"
    var conversionRate = 0f
    fun getApiResult(et_firstConversion :EditText,et_secondConversion :EditText,applicationContext:Context) {
        if (et_firstConversion != null && et_firstConversion.text.isNotEmpty() && et_firstConversion.text.isNotBlank()) {

            var API =
                "https://data.fixer.io/api/convert\n" +
                        "    ? access_key = e2827efeb746e3980024d56209c6c7c5" +"\n" +
                        "    & from = " +baseCurrency+"\n"+
                        "    & to = " +convertedToCurrency+"\n" +
                        "    & amount = "+et_firstConversion.text.toString()

            if (baseCurrency == convertedToCurrency) {
                Toast.makeText(
                    applicationContext,
                    "Please pick a currency to convert",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                GlobalScope.launch(Dispatchers.IO) {

                    try {

                        val apiResult = URL(API).readText()
                        val jsonObject = JSONObject(apiResult)
                        conversionRate =
                            jsonObject.getJSONObject("rates").getString(convertedToCurrency)
                                .toFloat()

                        Log.d("Main", "$conversionRate")
                        Log.d("Main", apiResult)

                        withContext(Dispatchers.Main) {
                            val text =
                                ((et_firstConversion.text.toString()
                                    .toFloat()) * conversionRate).toString()
                            et_secondConversion?.setText(text)

                        }

                    } catch (e: Exception) {
                        Log.e("Main", "$e")
                    }
                }
            }
        }
    }





}