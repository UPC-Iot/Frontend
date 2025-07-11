package com.example.protectify.presentation.addVisitor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FormVisitor(
    firstname: String,
    lastname: String,
    role: String,
    onFirstnameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onRoleChange: (String) -> Unit,
    onContinueClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    val isFormValid = firstname.isNotBlank() && lastname.isNotBlank() && role.isNotBlank()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Form Fields
        FormFields(
            firstname = firstname,
            lastname = lastname,
            role = role,
            onFirstnameChange = onFirstnameChange,
            onLastnameChange = onLastnameChange,
            onRoleChange = onRoleChange,
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.weight(1f))

        // Continue Button
        ContinueButton(
            enabled = isFormValid && !isLoading,
            isLoading = isLoading,
            onClick = onContinueClick
        )
    }
}

@Composable
private fun FormFields(
    firstname: String,
    lastname: String,
    role: String,
    onFirstnameChange: (String) -> Unit,
    onLastnameChange: (String) -> Unit,
    onRoleChange: (String) -> Unit,
    enabled: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Campo Nombre
        CustomTextField(
            label = "Nombre",
            value = firstname,
            onValueChange = onFirstnameChange,
            leadingIcon = Icons.Default.Person,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            enabled = enabled
        )

        // Campo Apellido
        CustomTextField(
            label = "Apellido",
            value = lastname,
            onValueChange = onLastnameChange,
            leadingIcon = Icons.Default.Person,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            enabled = enabled
        )

        // Parentesco Field
        CustomTextField(
            label = "Parentesco",
            value = role,
            onValueChange = onRoleChange,
            leadingIcon = Icons.Default.Face,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done
            ),
            enabled = enabled
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
            leadingIcon = {
                Icon(
                    leadingIcon,
                    contentDescription = null,
                    tint = if (enabled) Color.White.copy(alpha = 0.6f) else Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            },
            keyboardOptions = keyboardOptions,
            singleLine = true,
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
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
                Text(
                    text = "Procesando...",
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


