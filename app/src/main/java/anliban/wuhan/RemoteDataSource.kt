package anliban.wuhan

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface IRemoteDataSource {

    suspend fun getCityCoVs(): Result<List<CityCoV>, ErrorStatus>
}

class RemoteDataSource(
    private val api: ApiService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : IRemoteDataSource {

    override suspend fun getCityCoVs() = withContext(dispatcher) {
        try {
            val items = api.getCityCoVs().data
            return@withContext Result.success(items)
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext Result.error(ErrorStatus.SERVER_ERROR)
        }
    }
}