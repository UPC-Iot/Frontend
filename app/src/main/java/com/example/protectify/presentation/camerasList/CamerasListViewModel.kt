package com.example.protectify.presentation.camerasList

import android.devicelock.DeviceId
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.protectify.common.GlobalVariables
import com.example.protectify.common.Resource
import com.example.protectify.common.UIState
import com.example.protectify.data.repository.device.DeviceRepository
import com.example.protectify.data.repository.owner.OwnerRepository
import com.example.protectify.domain.device.Device
import com.example.protectify.domain.visitor.Visitor
import kotlinx.coroutines.launch

class CamerasListViewModel(
    private val navController: NavController,
    private val ownerRepository: OwnerRepository,
    private val deviceRepository: DeviceRepository
):ViewModel() {

    private val _state = mutableStateOf(UIState<List<Visitor>>())
    val state: State<UIState<List<Visitor>>> get() = _state

    private val _cameraState = mutableStateOf(UIState<List<Device>>())
    val camerasState: State<UIState<List<Device>>> get() = _cameraState

    fun goToBack() {
        navController.popBackStack()
    }

    fun getCameras() {
        _cameraState.value = UIState(isLoading = true)
        viewModelScope.launch {
            val ownerResult = ownerRepository.getOwnerByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            when (ownerResult) {
                is Resource.Success -> {
                    val ownerId = ownerResult.data?.id
                    if (ownerId != null) {
                        val result = deviceRepository.getAllDevicesByOwnerId(ownerId, GlobalVariables.TOKEN)
                        if (result is Resource.Success) {
                            val cameras = result.data?.filter { it.type == "CAMERA" } ?: emptyList()
                            _cameraState.value = UIState(data = cameras)
                        } else if (result is Resource.Error) {
                            _cameraState.value = UIState(message = result.message ?: "Error al obtener los dispositivos")
                        }
                    } else {
                        _cameraState.value = UIState(message = "No se encontró el propietario")
                    }
                }
                is Resource.Error -> {
                    _cameraState.value = UIState(message = ownerResult.message ?: "Error al obtener el propietario")
                }
            }
        }
    }

    fun deleteCamera(deviceId: String) {
        viewModelScope.launch {
            val result = deviceRepository.deleteDevice(deviceId.toLong(), GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                getCameras() // Refresca la lista de cámaras tras eliminar
            } else if (result is Resource.Error) {
                _cameraState.value = UIState(message = result.message ?: "Error al eliminar la cámara")
            }
        }
    }

}