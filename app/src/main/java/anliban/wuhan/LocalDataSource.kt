package anliban.wuhan

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

interface ILocalDataSource {

    fun saveCityCoVs(items: List<CityCoV>?)

    fun getCityCoVs(): List<CityCoV>?
}

class LocalDataSource(private val store: CovPref) : ILocalDataSource {

    private val gson = Gson()

    override fun saveCityCoVs(items: List<CityCoV>?) {
        store.cityCoV = gson.toJson(items)
    }

    override fun getCityCoVs(): List<CityCoV>? {
        val type = object : TypeToken<List<CityCoV>>() {}.type
        val result = gson.fromJson<List<CityCoV>>(store.cityCoV, type)
        return result ?: null
    }
}