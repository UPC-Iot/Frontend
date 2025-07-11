package com.example.protectify.presentation.alertList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.protectify.common.GlobalVariables
import com.example.protectify.common.Resource
import com.example.protectify.common.UIState
import com.example.protectify.data.repository.alert.AlertRepository
import com.example.protectify.data.repository.owner.OwnerRepository
import com.example.protectify.domain.alert.Alert
import kotlinx.coroutines.launch

class AlertListViewModel(
    private val navController: NavController,
    private val ownerRepository: OwnerRepository,
    private val alertRepository: AlertRepository
): ViewModel() {

    private val _alertState = mutableStateOf(UIState<List<Alert>>())
    val alertState: State<UIState<List<Alert>>> get() = _alertState

    fun goBack() {
        navController.popBackStack()
    }

    fun getAlerts() {
        _alertState.value = UIState(isLoading = true)
        viewModelScope.launch {
            val token = GlobalVariables.TOKEN
            val ownerResult = ownerRepository.getOwnerByUserId(GlobalVariables.USER_ID, token)
            when (ownerResult) {
                is Resource.Success -> {
                    val ownerId = ownerResult.data?.id
                    if (ownerId != null) {
                        val result = alertRepository.getAllAlertsByOwnerId(ownerId, token)
                        if (result is Resource.Success) {
                            _alertState.value = UIState(data = result.data ?: emptyList())
                        } else if (result is Resource.Error) {
                            _alertState.value = UIState(message = result.message ?: "Error al obtener las alertas")
                        }
                    } else {
                        _alertState.value = UIState(message = "No se encontró el propietario")
                    }
                }
                is Resource.Error -> {
                    _alertState.value = UIState(message = ownerResult.message ?: "Error al obtener propietario")
                }
            }
        }
    }

    fun deleteAlert(alertId: Long) {
        viewModelScope.launch {
            val token = GlobalVariables.TOKEN
            val result = alertRepository.deleteAlert(alertId, token)
            if (result is Resource.Success) {
                val alerts = _alertState.value.data?.toMutableList() ?: return@launch
                alerts.removeIf { it.id == alertId }
                _alertState.value = UIState(data = alerts)
            } else {
                _alertState.value = UIState(message = "Error al intentar eliminar la alerta")
            }
        }
    }


}