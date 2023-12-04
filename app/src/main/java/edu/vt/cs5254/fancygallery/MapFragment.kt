package edu.vt.cs5254.fancygallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceManager
import edu.vt.cs5254.fancygallery.databinding.FragmentMapBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapFragment: Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding
        get() = checkNotNull(_binding) {"FragmentMapBinding null"}

    private val vm: MapViewModel by viewModels()
    private val activityVM : MainViewModel by activityViewModels()

    //Add CoroutineScope
    private var mapCoroutineScope: CoroutineScope? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(Configuration.getInstance()){
            load(
                context,
                PreferenceManager.getDefaultSharedPreferences(requireContext())
            )
            userAgentValue = requireContext().packageName
        }

        binding.mapView.apply {
            minZoomLevel = 1.5
            maxZoomLevel = 15.0
            setTileSource(TileSourceFactory.MAPNIK)
            isVerticalMapRepetitionEnabled = false
            setScrollableAreaLimitLatitude(
                MapView.getTileSystem().maxLatitude,
                MapView.getTileSystem().minLatitude,
                0
            )
            isTilesScaledToDpi = true
            zoomController.setVisibility(
                CustomZoomButtonsController.Visibility.ALWAYS
            )
        }
//        activityVM.galleryItems.value.filter { it.latitude != 0.0 && it.longitude != 0.0 }
//            .forEach { galleryItem ->
//                val marker = Marker(binding.mapView).apply {
//                    position = GeoPoint(galleryItem.latitude, galleryItem.longitude)
//                    title = galleryItem.title
//                }
//                binding.mapView.overlays.add(marker)
//            }
        // 创建协程作用域
        mapCoroutineScope = CoroutineScope(Dispatchers.Main)

        // 在协程作用域内观察 StateFlow
        mapCoroutineScope?.launch {
            activityVM.galleryItems.collect { items ->
                // 这里可以放置您的标记代码
                val filteredItems = items.filter { it.latitude != 0.0 && it.longitude != 0.0 }
                for (galleryItem in filteredItems) {
                    val marker = Marker(binding.mapView).apply {
                        position = GeoPoint(galleryItem.latitude, galleryItem.longitude)
                        title = galleryItem.title
                    }
                    binding.mapView.overlays.add(marker)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // 取消协程作用域
        mapCoroutineScope?.cancel()
    }

    override fun onPause() {
        super.onPause()
        with(binding.mapView){
            vm.saveMapState(zoomLevelDouble, mapCenter)
            onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding.mapView){
            onResume()
            controller.setZoom(vm.zoomLevel)
            controller.setCenter(vm.mapCenter)
        }

        Log.w(
            "SHARED ACTIVITY VM TEST",
            "Found ${activityVM.galleryItems.value.size} gallery items!!!"
            )
    }
}