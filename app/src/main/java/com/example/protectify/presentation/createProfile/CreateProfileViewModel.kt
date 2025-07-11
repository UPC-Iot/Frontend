package com.example.protectify.presentation.createProfile

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.protectify.common.GlobalVariables
import com.example.protectify.common.Resource
import com.example.protectify.common.Routes
import com.example.protectify.common.UIState
import com.example.protectify.data.repository.profile.CloudStorageRepository
import com.example.protectify.data.repository.profile.ProfileRepository
import com.example.protectify.domain.profile.Profile
import com.example.protectify.presentation.auth.register.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateProfileViewModel(
    private val navController: NavController,
    private val profileRepository: ProfileRepository,
    private val cloudStorageRepository: CloudStorageRepository,
    private val registerViewModel: RegisterViewModel
): ViewModel() {

    // Variables de estado para los campos de texto
    var photoUrl = mutableStateOf("")
        private set
    var description = mutableStateOf("")
        private set
    var birthdate = mutableStateOf("")
        private set


    private val _state = mutableStateOf(UIState<Profile>())
    val state: State<UIState<Profile>> get() = _state

    fun createProfile() {
        viewModelScope.launch {
            val token = GlobalVariables.TOKEN
            if (token.isBlank()) {
                _state.value = UIState(message = "Un token es requerido")
                return@launch
            }

            if (!isValidDateFormat(birthdate.value)) {
                _state.value = UIState(message = "Formato de fecha inválido (usa YYYY-MM-DD)")
                return@launch
            }

            _state.value = UIState(isLoading = true)
            val profile = registerViewModel.getProfile().copy(
                description = description.value,
                photo = photoUrl.value,
                birthDate = birthdate.value
            )
            val result = profileRepository.createProfile(profile,token)
            withContext(Dispatchers.Main) {
                if (result is Resource.Success) {
                    _state.value = UIState(data = result.data)
                    registerViewModel.clearFields()
                    description.value = ""
                    photoUrl.value = ""
                    birthdate.value = ""
                    goToCreateHouse()
                } else {
                    _state.value = UIState(message = result.message ?: "Error al crear perfil")
                }
            }
        }
    }

    private fun isValidDateFormat(date: String): Boolean {
        // Regex para YYYY-MM-DD
        val regex = Regex("""\d{4}-\d{2}-\d{2}""")
        return regex.matches(date)
    }

    private fun goToCreateHouse() {
        navController.navigate(Routes.CreateHouse.route)
    }


    fun uploadImage(imageUri: Uri) {
        _state.value = UIState(isLoading = true)
        viewModelScope.launch {
            try {
                val filename = imageUri.lastPathSegment ?: "default_image_name"
                val imageUrl = cloudStorageRepository.uploadFile(filename, imageUri)
                photoUrl.value = imageUrl
                _state.value = UIState(isLoading = false)
            } catch (e: Exception) {
                _state.value = UIState(message = "Error uploading image: ${e.message}")
            }
        }
    }



}