package com.skywalker.test.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.skywalker.test.services.model.Photo
import com.skywalker.test.services.model.ResponseModel
import com.skywalker.test.services.repo.PhotosRepo
import kotlinx.coroutines.launch
private const val TAG = "HomeActivityViewModel";
class HomeActivityViewModel(private val photosRepo: PhotosRepo = PhotosRepo()) : ViewModel() {


    lateinit var responseModel: MutableLiveData<ResponseModel<List<Photo>>>;
    private var startIndex: Int = 0;
    private var endIndex: Int = 200;

    init {
        responseModel = MutableLiveData();
    }


    fun getPhotoApi(startIndex:Int,endIndex:Int) {

        viewModelScope.launch {

            try {
                val call = photosRepo.getPhotosList(startIndex, endIndex);
                var rm: ResponseModel<List<Photo>>? = null;
                if (call.isSuccessful) {
                    Log.e(TAG,"sucess")
                    rm = ResponseModel(200, call.body() as List<Photo>, "Success");
                    responseModel.postValue(rm!!);

                } else {
                    //failed
                    Log.e(TAG,"failed")
                    rm = ResponseModel(200, null, "Success");
                    responseModel.postValue(rm!!);

                }
            } catch (exc: Exception) {
                exc.printStackTrace()
            }

        }


    }

    fun loadMoreItems(){
        startIndex=endIndex;
        endIndex=startIndex+endIndex;
        getPhotoApi(startIndex,endIndex)
    }

}