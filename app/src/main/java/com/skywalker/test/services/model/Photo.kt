package com.skywalker.test.services.model

data class Photo (

    var albumId: Int? = null,
    var id: Int? = null,
    var title: String? = null,
    var url: String? = null,
    var thumbnailUrl: String? = null,
    var isDownloading:Boolean=false,
    var onDownlaodCompleted:Boolean=false

)