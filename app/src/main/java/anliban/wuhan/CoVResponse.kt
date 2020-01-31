package anliban.wuhan

import com.google.gson.annotations.SerializedName

data class CoVResponse(
    @SerializedName("data") val data: List<CityCoV>
)

data class CityCoV(
    @SerializedName("city") val city: String?,
    @SerializedName("update_time") val updateTIme: String,
    @SerializedName("death") val death: Int,
    @SerializedName("recover") val recover: Int,
    @SerializedName("id") val id: Int,
    @SerializedName("lat") val lat: Double,
    @SerializedName("long") val lng: Double,
    @SerializedName("confirm") val confirm: Int,
    @SerializedName("country") val country: String
)