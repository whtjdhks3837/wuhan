package anliban.wuhan

interface ICoVRepository {

    suspend fun getCityCoVsFromRemote(): Result<List<CityCoV>?, ErrorStatus>

    suspend fun getCityCoVsFromLocal(): List<CityCoV>?

    suspend fun fetchCityCoVs(): Result<List<CityCoV>, ErrorStatus>

    suspend fun saveCityCoVs(items: List<CityCoV>?)
}

class CoVRepository(
    private val remoteDataSource: IRemoteDataSource,
    private val localDataSource: ILocalDataSource
) : ICoVRepository {

    override suspend fun getCityCoVsFromRemote(): Result<List<CityCoV>, ErrorStatus> {
        return remoteDataSource.getCityCoVs()
    }

    override suspend fun getCityCoVsFromLocal(): List<CityCoV>? {
        return localDataSource.getCityCoVs()
    }

    override suspend fun fetchCityCoVs(): Result<List<CityCoV>, ErrorStatus> {
        return when (val result = remoteDataSource.getCityCoVs()) {
            is Result.Success -> result
            is Result.Error -> getCityCoVsFromLocal()?.let { Result.success(it) } ?: result
        }
    }

    override suspend fun saveCityCoVs(items: List<CityCoV>?) {
        localDataSource.saveCityCoVs(items)
    }
}