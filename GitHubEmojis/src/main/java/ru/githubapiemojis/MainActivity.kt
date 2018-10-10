package ru.githubapiemojis

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Activity
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.security.ProviderInstaller


class MainActivity : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var refreshLayout: SwipeRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getting recyclerview from xml
        recyclerView = findViewById(R.id.recyclerView)

        //adding a layoutmanager
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        refreshLayout = findViewById(R.id.refreshLayout)
        refreshLayout?.setOnRefreshListener {
            getEmojis()
        }

        val adapter = EmojisRecyclerViewAdapter(ArrayList())
        recyclerView?.adapter = adapter

        updateAndroidSecurityProvider(this)
    }

    fun getEmojis() {
        refreshLayout?.isRefreshing = true


        val apiService = GitHubApi.create()
        apiService.getEmojis().enqueue(object : Callback<JsonElement> {
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                refreshLayout?.isRefreshing = false
                Log.d("MY_TAG", "Error: " + t.localizedMessage)
            }

            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                refreshLayout?.isRefreshing = false

                if (response.body() != null) {
                    val emojisList = ArrayList<Emoji>()
                    val emojis = Gson().fromJson(response.body(), JsonElement::class.java) as JsonObject

                    for (key in emojis.keySet()) {
                        val emoji = Emoji(key, emojis.get(key).asString)
                        emojisList.add(emoji)
                    }

                    //creating our adapter
                    val adapter = EmojisRecyclerViewAdapter(emojisList)
                    //now adding the adapter to recyclerview
                    recyclerView?.adapter = adapter
                }
            }
        })
    }

    private fun updateAndroidSecurityProvider(callingActivity: Activity) {
        try {
            ProviderInstaller.installIfNeeded(callingActivity)
            getEmojis()
        } catch (e: GooglePlayServicesRepairableException) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0)
        } catch (e: GooglePlayServicesNotAvailableException) {
            Log.e("SecurityException", "Google Play Services not available.")
        }
    }
}
