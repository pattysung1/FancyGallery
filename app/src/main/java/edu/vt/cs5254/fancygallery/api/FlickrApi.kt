package edu.vt.cs5254.fancygallery.api

import retrofit2.http.GET

private const val API_KEY = "ff1438ee55eed82e3f386c609ac9f385"
interface FlickrApi {
    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=$API_KEY" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )
    suspend fun fetchPhotos(): String
}