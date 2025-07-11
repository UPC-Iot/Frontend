package com.example.protectify.presentation.createHouse

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.protectify.common.GlobalVariables
import com.example.protectify.common.Resource
import com.example.protectify.common.Routes
import com.example.protectify.common.UIState
import com.example.protectify.data.repository.house.HouseRepository
import com.example.protectify.data.repository.owner.OwnerRepository
import com.example.protectify.domain.house.CreateHouse
import com.example.protectify.domain.house.House
import kotlinx.coroutines.launch

class CreateHouseViewModel(
    private val navController: NavController,
    private val houseRepository: HouseRepository,
    private val ownerRepository: OwnerRepository
): ViewModel() {

    // Variables de estado para los campos de texto
    var name = mutableStateOf("")
        private set
    var address = mutableStateOf("")
        private set
    var description = mutableStateOf("")
        private set


    private val _state = mutableStateOf(UIState<House>())
    val state: State<UIState<House>> get() = _state

    private fun goToHome() {
        navController.navigate(Routes.Home.route)
    }

    fun createHouse() {
        viewModelScope.launch {
            val token = GlobalVariables.TOKEN
            val userId = GlobalVariables.USER_ID

            if (token.isBlank()) {
                _state.value = UIState(message = "Token requerido para crear la casa")
                return@launch
            }

            _state.value = UIState(isLoading = true)

            try {
                // Get owner by user ID
                val ownerResult = ownerRepository.getOwnerByUserId(userId, token)
                val ownerId = ownerResult.data?.id

                if (ownerId == null) {
                    _state.value = UIState(message = "No se pudo obtener la información del propietario")
                    return@launch
                }

                // Create house
                val house = CreateHouse(
                    ownerId = ownerId,
                    address = address.value,
                    name = name.value,
                    description = description.value
                )

                val result = houseRepository.createHouse(house, token)

                when (result) {
                    is Resource.Success -> {
                        _state.value = UIState(data = result.data)
                        goToHome()
                    }
                    is Resource.Error -> {
                        _state.value = UIState(message = result.message ?: "Error al crear la casa")
                    }
                }
            } catch (e: Exception) {
                _state.value = UIState(message = "Error inesperado: ${e.message}")
            }
        }
    }
}
