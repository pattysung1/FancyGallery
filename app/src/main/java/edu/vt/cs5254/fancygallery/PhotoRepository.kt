package edu.vt.cs5254.fancygallery

import edu.vt.cs5254.fancygallery.api.FlickrApi
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

class PhotoRepository {
    private val flickrApi: FlickrApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://www.flickr.com/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        flickrApi = retrofit.create(FlickrApi::class.java)
        // TODO: Not sure about create()
    }
    suspend fun fetchContents() = flickrApi.fetchPhotos()
}