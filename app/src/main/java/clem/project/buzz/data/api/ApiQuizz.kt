package clem.project.buzz.data.api

import clem.project.buzz.models.api.CategoriesResponse
import clem.project.buzz.models.api.QuizResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiQuizz {
    @GET("/api.php?amount=1&encode=base64")
    suspend fun getOneQuestion(): QuizResponse

    @GET("/api_category.php")
    suspend fun getCategories(): CategoriesResponse

    @GET("/api.php?amount=1&encode=base64&")
    suspend fun getOneQuestionByCategory(@Query("category") id: Int): QuizResponse
}
