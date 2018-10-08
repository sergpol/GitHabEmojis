package ru.githubapiemojis

import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.Call;
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by Полицинский Сергей on 08.10.2018.
 */
interface GitHubApi {
    var baseURL: String

    @GET("emojis")
    fun getEmojis() : Call<JsonElement>

    companion object Factory {
        fun create(): GitHubApi {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .baseUrl("https://api.github.com/")
                .build()

            return retrofit.create(GitHubApi::class.java);
        }
    }
}