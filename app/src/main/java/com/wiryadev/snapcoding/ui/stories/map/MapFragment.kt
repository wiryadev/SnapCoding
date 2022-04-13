package com.wiryadev.snapcoding.ui.stories.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wiryadev.snapcoding.databinding.FragmentMapBinding
import com.wiryadev.snapcoding.utils.showSnackbar

class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding

    private var mapView: MapView? = null
    private lateinit var gMap: GoogleMap


    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView = binding?.mapView

        mapView?.apply {
            onCreate(savedInstanceState)

            try {
                MapsInitializer.initialize(context.applicationContext)
            } catch (e: Exception) {
                binding?.root?.showSnackbar(e.message.toString())
            }

            getMapAsync { googleMap ->
                gMap = googleMap

                gMap.uiSettings.isZoomControlsEnabled = true
                gMap.uiSettings.isIndoorLevelPickerEnabled = true
                gMap.uiSettings.isCompassEnabled = true
                gMap.uiSettings.isMapToolbarEnabled = true

                val dicodingSpace = LatLng(-6.8957643, 107.6338462)
                gMap.addMarker(
                    MarkerOptions()
                        .position(dicodingSpace)
                        .title("Dicoding Space")
                        .snippet("Batik Kumeli No.50")
                )
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))
            }

        }
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

//    override fun onMapReady(googleMap: GoogleMap) {
//        gMap = googleMap
//
//        gMap.uiSettings.isZoomControlsEnabled = true
//        gMap.uiSettings.isIndoorLevelPickerEnabled = true
//        gMap.uiSettings.isCompassEnabled = true
//        gMap.uiSettings.isMapToolbarEnabled = true
//
//        val dicodingSpace = LatLng(-6.8957643, 107.6338462)
//        gMap.addMarker(
//            MarkerOptions()
//                .position(dicodingSpace)
//                .title("Dicoding Space")
//                .snippet("Batik Kumeli No.50")
//        )
//        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpace, 15f))
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}