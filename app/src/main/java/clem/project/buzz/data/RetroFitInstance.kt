package clem.project.buzz.data

import clem.project.buzz.data.api.ApiQuizz
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://opentdb.com"

    val api: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val quizApi: ApiQuizz by lazy {
        api.create(ApiQuizz::class.java)
    }
}