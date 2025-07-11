package com.example.protectify.presentation.visitorsList

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.protectify.common.GlobalVariables
import com.example.protectify.common.Resource
import com.example.protectify.common.UIState
import com.example.protectify.data.repository.owner.OwnerRepository
import com.example.protectify.data.repository.visitor.VisitorRepository
import com.example.protectify.domain.visitor.Visitor
import kotlinx.coroutines.launch

class VisitorsListViewModel(
    private val navController: NavController,
    private val ownerRepository: OwnerRepository,
    private val visitorRepository: VisitorRepository
): ViewModel() {

    private val _state = mutableStateOf(UIState<List<Visitor>>())
    val state: State<UIState<List<Visitor>>> get() = _state

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

    fun goToAddVisitorScreen() {
        navController.navigate("add-visitor")
    }

    fun goToHome() {
        navController.navigate("home")
    }

    fun deleteVisitor(visitorId: Long) {
        viewModelScope.launch {
            val token = GlobalVariables.TOKEN
            val result = visitorRepository.deleteVisitor(visitorId, token)
            when (result) {
                is Resource.Success -> {
                    Log.d("VisitorsListViewModel", "Visitor deleted successfully")
                    getVisitors() // Refresh the list after deletion
                }
                is Resource.Error -> {
                    Log.e("VisitorsListViewModel", "Error deleting visitor: ${result.message}")
                }
            }
        }
    }


}