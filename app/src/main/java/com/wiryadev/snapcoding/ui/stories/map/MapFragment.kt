package com.wiryadev.snapcoding.ui.stories.map

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.databinding.FragmentMapBinding
import com.wiryadev.snapcoding.model.Story
import com.wiryadev.snapcoding.model.asLatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding

    private var gMap: GoogleMap? = null

    private val viewModel by viewModels<MapViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val supportMapFragment = childFragmentManager.findFragmentById(
            R.id.google_map
        ) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding?.progressBar?.isVisible = uiState.isLoading

            if (uiState.stories.isNotEmpty()) {
                val nonNullLocations = uiState.stories.filter {
                    it.location != null
                }
                if (nonNullLocations.isNotEmpty()) {
                    addMarkers(nonNullLocations)
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap
        viewModel.getStoriesWithLocation()

        gMap?.uiSettings?.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }

        setMapStyle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addMarkers(locations: List<Story>) {
        locations.forEachIndexed { i, item ->
            val marker = gMap?.addMarker(
                MarkerOptions()
                    .position(item.location!!.asLatLng())
                    .title(item.name)
            )
            marker?.tag = item


            // animate camera to first item only
            if (i == 0) {
                gMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(item.location!!.asLatLng(), 5F)
                )
            }
        }

        gMap?.setOnInfoWindowClickListener { marker ->
            findNavController().navigate(
                MapFragmentDirections.actionNavigationMapToDetailFragment(
                    story = marker.tag as Story,
                )
            )
        }
    }

    private fun setMapStyle() {
        try {
            val success = gMap?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style)
            ) ?: false
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }
}

private const val TAG = "MapFragment"