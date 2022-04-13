package com.wiryadev.snapcoding.ui.stories.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.dataStore
import com.wiryadev.snapcoding.databinding.FragmentMapBinding
import com.wiryadev.snapcoding.ui.ViewModelFactory

class MapFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding

    private var gMap: GoogleMap? = null

    private val viewModel by viewModels<MapViewModel> {
        ViewModelFactory(UserPreference.getInstance(requireContext().dataStore), requireContext())
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

        val supportMapFragment = childFragmentManager.findFragmentById(
            R.id.google_map
        ) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        viewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                viewModel.getStoriesForMap(user.token)
            }
        }

        viewModel.uiState.observe(viewLifecycleOwner) { uiState ->
            binding?.progressBar?.isVisible = uiState.isLoading

            if (uiState.stories.isNotEmpty()) {
                val nonNullLocations = uiState.stories.filter {
                    it.lat != null && it.lon != null
                }
                if (nonNullLocations.isNotEmpty()) {
                    val storyLocations = nonNullLocations.map {
                        StoryLocation(
                            name = it.name,
                            location = LatLng(it.lat!!, it.lon!!)
                        )
                    }
                    addMarkers(storyLocations)
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        gMap = googleMap

        gMap?.uiSettings?.apply {
            isZoomControlsEnabled = true
            isIndoorLevelPickerEnabled = true
            isCompassEnabled = true
            isMapToolbarEnabled = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addMarkers(locations: List<StoryLocation>) {
        locations.forEachIndexed { i, item ->
            gMap?.addMarker(
                MarkerOptions()
                    .position(item.location)
                    .title(item.name)
            )

            // animate camera to first item only
            if (i == 0) {
                gMap?.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(item.location, 5F)
                )
            }
        }
    }

    private data class StoryLocation(
        val name: String,
        val location: LatLng,
    )
}