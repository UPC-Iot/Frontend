package com.example.protectify.presentation.createProfile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TextFieldDefaults
import com.example.protectify.presentation.addVisitorImage.ProfileImageSection


@Composable
fun CreateProfileScreen(
    viewModel: CreateProfileViewModel,
    modifier: Modifier = Modifier
) {
    val photoUrl by viewModel.photoUrl
    val description by viewModel.description
    val birthdate by viewModel.birthdate
    val state by viewModel.state

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadImage(it) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF26272C))
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,

        ) {
            Text(
                text = "Crear Perfil",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ProfileImageSection(
            photoUrl = photoUrl,
            isLoading = state.isLoading,
            onImageClick = { imagePickerLauncher.launch("image/*") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo descripción
        OutlinedTextField(
            value = description,
            onValueChange = { viewModel.description.value = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFFBF4D36),
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo fecha de nacimiento solo texto
        OutlinedTextField(
            value = birthdate,
            onValueChange = { viewModel.birthdate.value = it },
            label = { Text("Fecha de nacimiento") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFFBF4D36),
                unfocusedIndicatorColor = Color.Gray,
                cursorColor = Color.White
            )
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón continuar
        Button(
            onClick = { viewModel.createProfile() },
            enabled = !state.isLoading && birthdate.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBF4D36))
        ) {
            Text(
                text = if (state.isLoading) "Creando perfil..." else "Crear Perfil",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (state.message.isNotEmpty()) {
            Text(
                text = state.message,
                color = Color.Red,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}