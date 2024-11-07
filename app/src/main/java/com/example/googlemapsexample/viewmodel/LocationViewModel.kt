package com.example.googlemapsexample.viewmodel

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.googlemapsexample.data.LocationData
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import kotlin.coroutines.jvm.internal.CompletedContinuation.context

class LocationViewModel: ViewModel() {

    val currentLocationData: Any
    private val _userLocationData = MutableStateFlow<LocationData?>(null)
    val userLocationData = _userLocationData.asStateFlow()
    private val _userLocationAddress = MutableStateFlow("")
    val userLocationAddress = _userLocationAddress.asStateFlow()

    private val _pickedLocationData = MutableStateFlow<LocationData?>(null)
    val pickedLocationData = _pickedLocationData.asStateFlow()
    private val _pickedLocationAddress= MutableStateFlow("")
    val pickedLocationAddress = _pickedLocationAddress.asStateFlow()

    private val fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(context)

    private val _locationState = mutableStateOf<LatLng?>(null)
    val locationState: State<LatLng?> = _locationState

    fun getCurrentLocation(context: Context){
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let{
                    _locationState.value = Latlng(it.latitude, it.latitude)
                }
            }
    }

    fun updatePickedLocation(latLng: LatLng){
        _pickedLocationData.value = LocationData(latitude = latLng.latitude, longitude = latLng.longitude)
    }

    fun updateUserLocation(lastLocation: Location?) {
        if (lastLocation != null){
            _userLocationData.value = LocationData(lastLocation.latitude, lastLocation.longitude)
        }
    }

    fun setUserAddress(address: String) {
        _userLocationAddress.value = address
    }

    fun setPickedLocationAddress(address: String) {
        _pickedLocationAddress.value = address
    }

    var navigateToMainScreen by mutableStateOf(false)

    fun onNavigateToMainScreen(){
        navigateToMainScreen = true
    }

    fun onNavigatedToMainScreen(){
        navigateToMainScreen = false
    }
}