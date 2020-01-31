package anliban.wuhan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CoVMapsViewModel(private val useCase: ICoVUseCase) : ViewModel(), CoroutineScope {

    private val job = Job()
    override val coroutineContext = Dispatchers.Main + job

    private val _cityCoVs = MutableLiveData<List<CityCoV>>()
    val cityCoVs: LiveData<List<CityCoV>> = _cityCoVs
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun fetchCityCoVs() {
        launch {
            when (val result = useCase.fetchCityCoVs()) {
                is Result.Success -> _cityCoVs.value = result.data
                is Result.Error -> _message.value = "데이터 로드 실패"
            }
        }
    }
}

class CoVMapsViewModelFactory(private val useCase: ICoVUseCase) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CoVMapsViewModel(useCase) as T
    }
}