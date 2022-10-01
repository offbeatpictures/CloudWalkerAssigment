package com.skywalker.test.services.repo

import com.skywalker.test.services.interfaces.ApiInterface
import com.skywalker.test.services.model.Photo
import com.skywalker.test.services.network.RetroHelper
import com.skywalker.test.viewmodel.HomeActivityViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response

//collection of properties
class PhotosRepo {

  suspend fun getPhotosList(startIndex:Int,endIndex:Int): Response<List<Photo>> = RetroHelper.apiInterface.getPhotosList(startIndex,endIndex);

}