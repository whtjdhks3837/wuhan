package anliban.wuhan

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import anliban.wuhan.databinding.ActivityMapsBinding
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
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
        binding.apply {
            lifecycleOwner = this@CovMapsActivity
            confirm.text = getString(R.string.confirm)
            death.text = getString(R.string.death)
        }
        initMaps()
        initViewModel()
        initAdmob()

        viewModel.fetchCityCoVs()
        viewModel.cityCoVs.observe(this, Observer { items ->
            val totalConfirm = items.sumBy { it.confirm }
            val totalDeath = items.sumBy { it.death }
            binding.confirm.text = "확진: $totalConfirm"
            binding.death.text = "사망: $totalDeath"
            items.forEach {
                map.addMarker(createMarker(it))
            }
        })
        viewModel.message.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun initMaps() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

    private fun initAdmob() {
        MobileAds.initialize(this, getString(R.string.admob))
        AdView(this).apply {
            adSize = AdSize.BANNER
            adUnitId = if (BuildConfig.DEBUG) {
                getString(R.string.banner_ad_unit_id_for_test)
            } else {
                getString(R.string.banner_ad_unit_id)
            }
            loadAd(createAdRequset())
        }.run {
            binding.adArea.addView(this)
        }

    }

    private fun createAdRequset() = if (BuildConfig.DEBUG) {
        AdRequest.Builder().addTestDevice("392A9D97FB45A2353BE98BD39815D0F9").build()
    } else {
        AdRequest.Builder().build()
    }

    private fun createMarker(cityCoV: CityCoV) =
        MarkerOptions()
            .position(LatLng(cityCoV.lat, cityCoV.lng))
            .icon(getIcon(cityCoV.confirm))
            .title("확진자 : ${cityCoV.confirm}")

    private fun getIcon(confirm: Int): BitmapDescriptor {
        val size = resizeIcon(confirm)
        val bitmap = (resources.getDrawable(R.drawable.circle) as BitmapDrawable).bitmap
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, size, size, false)
        return BitmapDescriptorFactory.fromBitmap(scaledBitmap)
    }

    // 수식 정리 요망
    private fun resizeIcon(confirm: Int): Int {
        return when (confirm) {
            in 0..100 -> 70
            in 101..500 -> 80
            in 501..3000 -> 90
            in 3000..5000 -> 100
            else -> 130
        }
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
        map.moveCamera(CameraUpdateFactory.newLatLng(LatLng(36.083, 117.261)))
    }
}
