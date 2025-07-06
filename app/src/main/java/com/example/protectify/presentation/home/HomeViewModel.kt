package com.example.protectify.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.protectify.common.GlobalVariables
import com.example.protectify.common.Resource
import com.example.protectify.common.UIState
import com.example.protectify.data.repository.device.DeviceRepository
import com.example.protectify.data.repository.notification.NotificationRepository
import com.example.protectify.data.repository.owner.OwnerRepository
import com.example.protectify.data.repository.profile.ProfileRepository
import com.example.protectify.data.repository.visitor.VisitorRepository
import com.example.protectify.domain.device.Device
import com.example.protectify.domain.profile.Profile
import com.example.protectify.domain.visitor.Visitor
import kotlinx.coroutines.launch

class HomeViewModel(
    private val navController: NavController,
    private val ownerRepository: OwnerRepository,
    private val profileRepository: ProfileRepository,
    private val visitorRepository: VisitorRepository,
    private val notificationRepository: NotificationRepository,
    private val deviceRepository: DeviceRepository
): ViewModel() {

    private val _state = mutableStateOf(UIState<List<Visitor>>())
    val state: State<UIState<List<Visitor>>> get() = _state

    private val _notificationCount = mutableIntStateOf(0)
    val notificationCount: State<Int> get() = _notificationCount

    private val _profileState = mutableStateOf(UIState<Profile>())
    val profileState: State<UIState<Profile>> get() = _profileState

    private val _cameraState = mutableStateOf(UIState<List<Device>>())
    val camerasState: State<UIState<List<Device>>> get() = _cameraState

    fun getVisitors() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val token = GlobalVariables.TOKEN
            val ownerResult = ownerRepository.getOwnerByUserId(GlobalVariables.USER_ID, token)
            when (ownerResult) {
                is Resource.Success -> {
                    val ownerId = ownerResult.data?.id
                    if (ownerId != null) {
                        val visitorsResult = visitorRepository.getAllVisitorsByOwnerId(ownerId, token)
                        when (visitorsResult) {
                            is Resource.Success -> {
                                _state.value = UIState(data = visitorsResult.data)
                            }
                            is Resource.Error -> {
                                _state.value = UIState(message = visitorsResult.message ?: "Error al obtener visitantes")
                            }
                        }
                    } else {
                        _state.value = UIState(message = "No se encontró el propietario")
                    }
                }
                is Resource.Error -> {
                    _state.value = UIState(message = ownerResult.message ?: "Error al obtener propietario")
                }
            }
        }
    }

    fun getProfile() {
        _profileState.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = profileRepository.getProfileByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                _profileState.value = UIState(data = result.data)
            } else if (result is Resource.Error) {
                _profileState.value = UIState(message = result.message ?: "Error al obtener el perfil")
            }
        }
    }

    fun goToAddVisitorScreen() {
        navController.navigate("add-visitor")
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


    fun loadNotificationCount() {
        viewModelScope.launch {
            val result = notificationRepository.getAllNotificationsByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            _notificationCount.value = (result.data?.size ?: 0).toInt()
        }
    }


}