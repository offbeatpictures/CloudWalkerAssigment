package com.skywalker.test.services.interfaces

import com.skywalker.test.services.model.Photo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("photos")
    suspend fun getPhotosList(@Query("_start")  _start:Int,@Query("_end") _end:Int): Response<List<Photo>>;

}