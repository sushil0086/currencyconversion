package com.currency.conversion

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
    private var tag = this.javaClass.name;
    var baseCurrency = "EUR"
    var convertedToCurrency = "USD"
    var conversionRate = 0f
    fun getApiResult(et_firstConversion :EditText,et_secondConversion :EditText,applicationContext:Context) {
        if (et_firstConversion.text.isNotEmpty() && et_firstConversion.text.isNotBlank()) {
            var API =
                "https://data.fixer.io/api/convert\n" +
                        "    ? access_key = f4a89dbe68e1ac2f5f8fe338d38f84cd" +"\n" +
                        "    & from = " +baseCurrency+"\n"+
                        "    & to = " +convertedToCurrency+"\n" +
                        "    & amount = "+et_firstConversion.text.toString()

            if (baseCurrency == convertedToCurrency) {
                Toast.makeText(
                    applicationContext,
                    applicationContext.getString(R.string.pls_pick_currency_convertor),
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

                        Log.d(tag, "$conversionRate")
                        Log.d(tag, apiResult)

                        withContext(Dispatchers.Main) {
                            val text =
                                ((et_firstConversion.text.toString()
                                    .toFloat()) * conversionRate).toString()
                            et_secondConversion?.setText(text)

                        }

                    } catch (e: Exception) {
                        Log.e(tag, "$e")
                    }
                }
            }
        }
    }
}