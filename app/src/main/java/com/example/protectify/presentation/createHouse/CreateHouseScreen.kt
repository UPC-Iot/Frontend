package com.example.protectify.presentation.createHouse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CreateHouseScreen(
    viewModel: CreateHouseViewModel,
    modifier: Modifier = Modifier
) {
    val name by viewModel.name
    val address by viewModel.address
    val description by viewModel.description
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF26272C))
    ) {
        // Header
        HeaderSection()

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Form Fields
            FormSection(
                name = name,
                address = address,
                description = description,
                onNameChange = { viewModel.name.value = it },
                onAddressChange = { viewModel.address.value = it },
                onDescriptionChange = { viewModel.description.value = it },
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            ContinueButton(
                enabled = name.isNotBlank() && address.isNotBlank() && !isLoading,
                isLoading = isLoading,
                onClick = {
                    isLoading = true
                    viewModel.createHouse()
                    isLoading = false
                }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HeaderSection(
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Title
        Text(
            text = "Registrar Casa",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}

@Composable
private fun FormSection(
    name: String,
    address: String,
    description: String,
    onNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    enabled: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // House Name Field
        CustomTextField(
            label = "Nombre de la Casa",
            value = name,
            onValueChange = onNameChange,
            leadingIcon = Icons.Default.Home,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            placeholder = "Ej: Casa Principal"
        )

        // Address Field
        CustomTextField(
            label = "Direccion",
            value = address,
            onValueChange = onAddressChange,
            leadingIcon = Icons.Default.LocationOn,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            enabled = enabled,
            placeholder = "Ej: Calle 123, Colonia Centro"
        )

        // Description Field
        CustomTextField(
            label = "Descripcion",
            value = description,
            onValueChange = onDescriptionChange,
            leadingIcon = Icons.Default.Info,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Done
            ),
            enabled = enabled,
            placeholder = "Descripción opcional de la casa",
            maxLines = 3
        )
    }
}

@Composable
private fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    enabled: Boolean = true,
    placeholder: String = "",
    maxLines: Int = 1,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        // Label
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.White.copy(alpha = if (enabled) 0.7f else 0.4f),
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // TextField
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.White.copy(alpha = 0.5f)
                )
            },
            leadingIcon = {
                Icon(
                    leadingIcon,
                    contentDescription = null,
                    tint = if (enabled) Color.White.copy(alpha = 0.6f) else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            },
            keyboardOptions = keyboardOptions,
            singleLine = maxLines == 1,
            maxLines = maxLines,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.Gray,
                focusedBorderColor = Color(0xFFE17055),
                unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                disabledBorderColor = Color.Gray.copy(alpha = 0.3f),
                cursorColor = Color(0xFFE17055),
                focusedLeadingIconColor = Color(0xFFE17055),
                unfocusedLeadingIconColor = Color.White.copy(alpha = 0.6f),
                disabledLeadingIconColor = Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
private fun ContinueButton(
    enabled: Boolean,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE17055),
            disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        if (isLoading) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Registrando casa...",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                )
            }
        } else {
            Text(
                text = "Continue",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 16.sp
                )
            )
        }
    }
}

