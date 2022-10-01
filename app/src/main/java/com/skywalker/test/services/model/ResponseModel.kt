package com.skywalker.test.services.model

data class ResponseModel<T>(
    var resultCode:Int,
    var response:T?,
    var responseMessage:String
)
