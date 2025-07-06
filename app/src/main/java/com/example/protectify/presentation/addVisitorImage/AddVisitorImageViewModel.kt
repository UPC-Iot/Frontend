package com.example.protectify.presentation.addVisitorImage

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.protectify.common.GlobalVariables
import com.example.protectify.common.Resource
import com.example.protectify.common.UIState
import com.example.protectify.data.repository.profile.CloudStorageRepository
import com.example.protectify.data.repository.visitor.VisitorRepository
import com.example.protectify.domain.visitor.Visitor
import com.example.protectify.presentation.addVisitor.AddVisitorViewModel
import kotlinx.coroutines.launch

class AddVisitorImageViewModel(
    private val navController: NavController,
    private val visitorRepository: VisitorRepository,
    private val addVisitorViewModel: AddVisitorViewModel,
    private val cloudStorageRepository: CloudStorageRepository
): ViewModel() {


    var photoUrl = mutableStateOf("")
        private set

    private val _state = mutableStateOf(UIState<Visitor>())
    val state: State<UIState<Visitor>> get() = _state

    fun createVisitor() {
        viewModelScope.launch {
            val token = GlobalVariables.TOKEN
            if (token.isBlank()) {
                _state.value = UIState(message = "Un token es requerido")
                return@launch
            }

            _state.value = UIState(isLoading = true)
            addVisitorViewModel.getVisitor { visitor ->
                if (visitor != null) {
                    val visitorWithPhoto = visitor.copy(photo = photoUrl.value)
                    viewModelScope.launch {
                        val result = visitorRepository.createVisitor(visitorWithPhoto, token)
                        when (result) {
                            is Resource.Success -> {
                                _state.value = UIState(data = result.data)
                                photoUrl.value = "" // Limpia la foto después de crear el visitante
                                addVisitorViewModel.clearFields()
                                goToVisitorsListScreen()
                            }
                            is Resource.Error -> {
                                _state.value = UIState(message = result.message ?: "Error al crear visitante")
                            }
                        }
                    }
                } else {
                    _state.value = UIState(message = "No se pudo crear el visitante")
                }
            }
        }
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

    fun goToVisitorsListScreen() {
        navController.navigate("visitor-list")
    }

    fun goToAddVisitorScreen() {
        navController.navigate("add-visitor")
    }


}