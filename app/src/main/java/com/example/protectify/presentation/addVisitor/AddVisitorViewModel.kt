// src/main/java/com/example/protectify/presentation/addVisitor/AddVisitorViewModel.kt

package com.example.protectify.presentation.addVisitor

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.protectify.common.GlobalVariables
import com.example.protectify.data.repository.house.HouseRepository
import com.example.protectify.data.repository.owner.OwnerRepository
import com.example.protectify.domain.visitor.CreateVisitor
import kotlinx.coroutines.launch

class AddVisitorViewModel(
    private val navController: NavController,
    private val ownerRepository: OwnerRepository,
    private val houseRepository: HouseRepository
) : ViewModel() {

    // Variables de estado para los campos del visitante
    var firstname = mutableStateOf("")
        private set
    var lastname = mutableStateOf("")
        private set
    var role = mutableStateOf("")
        private set

    // Funciones para actualizar los campos
    fun onFirstnameChange(value: String) { firstname.value = value }
    fun onLastnameChange(value: String) { lastname.value = value }
    fun onRoleChange(value: String) { role.value = value }

    // Función para obtener el objeto CreateVisitor con los valores actuales
    fun getVisitor(onResult: (CreateVisitor?) -> Unit) {
        viewModelScope.launch {
            val ownerResult = ownerRepository.getOwnerByUserId(GlobalVariables.USER_ID, GlobalVariables.TOKEN)
            val ownerId = ownerResult.data?.id
            if (ownerId != null) {
                val housesResult = houseRepository.getHousesByOwnerId(ownerId, GlobalVariables.TOKEN)
                val houseId = housesResult.data?.firstOrNull()?.id
                if (houseId != null) {
                    val visitor = CreateVisitor(
                        houseId = houseId,
                        firstname = firstname.value,
                        lastname = lastname.value,
                        photo = "", // Se asigna en otra pantalla
                        role = role.value,
                        lastVisit = null
                    )
                    onResult(visitor)
                    return@launch
                }
            }
            onResult(null)
        }
    }

    fun clearFields() {
        firstname.value = ""
        lastname.value = ""
        role.value = ""
    }

    fun goToAddVisitorImageScreen() {
        navController.navigate("add-visitor-image")
    }

    fun goToVisitorsList() {
        navController.navigate("visitor-list")
    }

}