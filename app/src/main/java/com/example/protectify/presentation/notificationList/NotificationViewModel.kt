package com.example.protectify.presentation.notificationList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.protectify.common.GlobalVariables
import com.example.protectify.common.Resource
import com.example.protectify.common.UIState
import com.example.protectify.data.repository.notification.NotificationRepository
import com.example.protectify.domain.notification.Notification
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val navController: NavController,
    private val notificationRepository: NotificationRepository
): ViewModel() {

    private val _state = mutableStateOf(UIState<List<Notification>>())
    val state: State<UIState<List<Notification>>> get() = _state

    fun goBack() {
        navController.popBackStack()
    }

    fun getNotificationList() {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            val result = notificationRepository.getAllNotificationsByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                val notifications = result.data?.sortedByDescending { it.date } ?: run {
                    _state.value = UIState(message = "No se encontraron notificaciones")
                    return@launch
                }
                _state.value = UIState(data = notifications)
            } else {
                _state.value = UIState(message = "Error al intentar obtener las notificaciones")
            }
        }
    }

    fun deleteNotification(notificationId: Long) {
        viewModelScope.launch {
            val result = notificationRepository.deleteNotification(notificationId, GlobalVariables.TOKEN)
            if (result is Resource.Success) {
                val notifications = _state.value.data?.toMutableList() ?: return@launch
                notifications.removeIf { it.id == notificationId }
                _state.value = UIState(data = notifications)
            } else {
                _state.value = UIState(message = "Error al intentar eliminar la notificación")
            }
        }
    }


}