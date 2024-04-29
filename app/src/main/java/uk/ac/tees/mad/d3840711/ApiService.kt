package uk.ac.tees.mad.d3840711

import retrofit2.Response
import retrofit2.http.GET

interface NewsApiService {

    @GET("/v2/top-headlines?sources=bbc-news&apiKey=c6d61cc942f74d02a1cc2ba8422cfdb2")
    suspend fun getLatestNews() : Response<latestNewsResponses>

}