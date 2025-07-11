package com.example.protectify.presentation.auth.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.protectify.common.GlobalVariables
import com.example.protectify.common.Resource
import com.example.protectify.common.Routes
import com.example.protectify.common.UIState
import com.example.protectify.data.repository.authentication.AuthenticationRepository
import com.example.protectify.domain.profile.CreateProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RegisterViewModel(
    private val navController: NavController,
    private val authenticationRepository: AuthenticationRepository,

    ): ViewModel() {

    private val _state = mutableStateOf(UIState<Unit>())
    val state: State<UIState<Unit>> get() = _state

    var firstName = mutableStateOf("")
        private set
    var lastName = mutableStateOf("")
        private set
    var phone = mutableStateOf("")
        private set
    var email = mutableStateOf("")
        private set
    var password = mutableStateOf("")
        private set

    fun getProfile(): CreateProfile {
        return CreateProfile(
            userId = GlobalVariables.USER_ID,
            firstName = firstName.value,
            lastName = lastName.value,
            birthDate = "",
            description = null,
            photo = null,
            phone = phone.value,
            address = null
        )
    }

    fun gotoBack(){
        navController.popBackStack()
    }

    private fun signIn(email: String, password: String) {
        viewModelScope.launch {
            val result = authenticationRepository.signIn(email, password)
            if (result is Resource.Success) {
                val token = result.data?.token
                if (token != null) {
                    GlobalVariables.USER_ID = result.data.id
                    GlobalVariables.TOKEN = token
                    withContext(Dispatchers.Main) {
                        goToCreateProfileScreen()
                    }
                } else {
                    _state.value = UIState(message = "Error al obtener el token")
                }
            } else {
                _state.value = UIState(message = result.message ?: "Error al iniciar sesión")
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _state.value = UIState(isLoading = true)
            val emailValue = email.value
            val passwordValue = password.value
            GlobalVariables.ROLES = listOf("ROLE_USER", "ROLE_OWNER")
            val roles = GlobalVariables.ROLES
            val result = authenticationRepository.signUp(emailValue, passwordValue, roles)
            if (result is Resource.Success) {
                _state.value = UIState(data = Unit)

                // Sign in the user to obtain the token
                signIn(emailValue, passwordValue)
            } else {
                _state.value = UIState(message = result.message ?: "Error al registrarse")
            }
        }
    }

    fun goToLoginScreen() {
        navController.navigate(Routes.Login.route)
    }

    private fun goToCreateProfileScreen() {
        navController.navigate(Routes.CreateProfile.route)
    }

    fun clearFields() {
        firstName.value = ""
        lastName.value = ""
        phone.value = ""
        email.value = ""
        password.value = ""
    }

}