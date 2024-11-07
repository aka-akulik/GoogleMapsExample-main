package com.example.googlemapsexample.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.data.EmptyGroup.location
import androidx.compose.ui.tooling.data.UiToolingDataApi
import com.example.googlemapsexample.utils.LocationUtils
import com.example.googlemapsexample.viewmodel.LocationViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(UiToolingDataApi::class)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnrememberedMutableState")
@Composable
fun MapScreen(viewModel: LocationViewModel, locationUtils: LocationUtils) {
    val pickedLocation by viewModel.pickedLocationData.collectAsState()
    val userLocation by viewModel.userLocationData.collectAsState()
    val currentLocation by viewModel.currentLocationData.collectAsState()
    val latLng = if (userLocation == null) LatLng(0.0, 0.0) else LatLng(
        userLocation!!.latitude,
        userLocation!!.longitude
    )
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latLng, 10f)
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            cameraPositionState = cameraPositionState,
            onMapClick = {
                viewModel.updatePickedLocation(it)
                locationUtils.getAddressFromPickedLocation(viewModel)
            }
        ) {
            val tempLatLng = if (pickedLocation == null) latLng else LatLng(
                pickedLocation!!.latitude,
                pickedLocation!!.longitude
            )
            Marker(
                state = MarkerState(
                    position = tempLatLng
                )
            )
            currentLocation?.let {location?.let ->
                Marker(
                    position = location,
                    title = "Your location"
                )}
        }

        Button(onClick = { viewModel.onNavigateToMainScreen() }) {
            Text("Go to Main Screen")
        }

    }
}