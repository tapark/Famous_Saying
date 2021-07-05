package com.example.todays_famous_saying

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.absoluteValue

class MainActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy {
        findViewById<ViewPager2>(R.id.viewPager)
    }

    private val progressBar: ProgressBar by lazy {
        findViewById<ProgressBar>(R.id.progressBar)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initData()
    }

    private fun initViews() {
        viewPager.setPageTransformer { page, position ->
            when {
                position.absoluteValue >= 0.5F -> { page.alpha = 0F }
                else -> { page.alpha = 1F - 2 * position.absoluteValue }
            }
        }
    }


    private fun initData() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(
            remoteConfigSettings {
                minimumFetchIntervalInSeconds = 0
            }
        )
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            progressBar.visibility = View.GONE
            if (it.isSuccessful) {
                val quotes = parseQuotesJson(remoteConfig.getString("quotes"))
                val isNameOn = remoteConfig.getBoolean("is_name_on")
                displayQuotesPager(quotes, isNameOn)
            }
        }
    }

    private fun parseQuotesJson(json: String): List<Quote> {
        val jsonArray = JSONArray(json)
        val jsonList = emptyList<JSONObject>().toMutableList()
        for (index in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(index)
            jsonObject?.let {
                jsonList += it
            }
        }

        return jsonList.map {
            Quote(
                quote = it.getString("quote"),
                name = it.getString("name")
            )
        }
    }

    private fun displayQuotesPager(quotes: List<Quote>, isNameOn: Boolean) {
        viewPager.adapter = QuotesPagerAdaptor(quotes, isNameOn)
        val startingPoint: Int = quotes.size * 10000
        viewPager.setCurrentItem(startingPoint, false)
    }
}