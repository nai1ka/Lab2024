package ru.ndevelop.tinkofflab2024

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.ndevelop.tinkofflab2024.models.Movie
import ru.ndevelop.tinkofflab2024.models.Response

object ApiClient {
    val BASE_URL = "https://kinopoiskapiunofficial.tech"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    val apiService: MyApiEndpointInterface by lazy {
        retrofit.create(MyApiEndpointInterface::class.java)
    }
}


interface MyApiEndpointInterface {

    @Headers("x-api-key: e30ffed0-76ab-4dd6-b41f-4c9da2b2735b")
    @GET("/api/v2.2/films/top")
    fun getTop(@Query("type") type: String): Call<Response>

    @Headers("x-api-key: e30ffed0-76ab-4dd6-b41f-4c9da2b2735b")
    @GET("/api/v2.2/films/{filmId}")
    fun getFilmInfo(@Path("filmId") filmId: Int): Call<Movie>

}