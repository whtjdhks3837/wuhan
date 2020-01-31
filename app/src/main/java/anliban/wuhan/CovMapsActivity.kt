package anliban.wuhan

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import anliban.wuhan.databinding.ActivityMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CovMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var viewModel: CoVMapsViewModel
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        initMaps()
        initViewModel()

        viewModel.fetchCityCoVs()
        viewModel.cityCoVs.observe(this, Observer { items ->
            val totalRecover = items.sumBy { it.recover }
            val totalDeath = items.sumBy { it.death }
            binding.recover.text = totalRecover.toString()
            binding.death.text = totalDeath.toString()
            items.forEach {
                map.addMarker(MarkerOptions().position(LatLng(it.lng, it.lng)))
            }
        })
        viewModel.message.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initViewModel() {
        val pref = CovPref(applicationContext.getSharedPreferences("local", Context.MODE_PRIVATE))
        val remoteDataSource = RemoteDataSource(Api.INSTANCE)
        val localDataSource = LocalDataSource(pref)
        val repository = CoVRepository(remoteDataSource, localDataSource)
        val useCase = CoVUseCase(repository)
        val viewModelFactory = CoVMapsViewModelFactory(useCase)
        viewModel = ViewModelProvider(this, viewModelFactory)[CoVMapsViewModel::class.java]
    }

    private fun initMaps() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
    }
}
