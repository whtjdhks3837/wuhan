package anliban.wuhan

import retrofit2.http.GET

interface ApiService {

    @GET("/default/wuhanLambda")
    suspend fun getCityCoVs(): CoVResponse
}