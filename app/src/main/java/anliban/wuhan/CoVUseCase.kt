package anliban.wuhan

import com.google.android.gms.maps.model.LatLng

interface ICoVUseCase {

    suspend fun fetchCityCoVs(): Result<List<CityCoV>, ErrorStatus>
}

class CoVUseCase(private val repository: ICoVRepository) : ICoVUseCase {

    override suspend fun fetchCityCoVs(): Result<List<CityCoV>, ErrorStatus> {
        return repository.fetchCityCoVs()
    }
}