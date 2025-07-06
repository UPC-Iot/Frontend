package com.example.protectify.presentation.addVisitor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.protectify.presentation.addVisitor.components.FormVisitor

@Composable
fun AddVisitorScreen(
    viewModel: AddVisitorViewModel,
    modifier: Modifier = Modifier
) {
    val firstname by viewModel.firstname
    val lastname by viewModel.lastname
    val role by viewModel.role
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF26272C))
    ) {
        // Header
        HeaderSection(
            onBackClick = {
                viewModel.goToVisitorsList()
            }
        )

        // Content with Form
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Form Visitor Component
            FormVisitor(
                firstname = firstname,
                lastname = lastname,
                role = role,
                onFirstnameChange = viewModel::onFirstnameChange,
                onLastnameChange = viewModel::onLastnameChange,
                onRoleChange = viewModel::onRoleChange,
                onContinueClick = {
                    isLoading = true
                    viewModel.getVisitor { visitor ->
                        isLoading = false
                        if (visitor != null) {
                            viewModel.goToAddVisitorImageScreen()
                        } else {
                            // TODO: Mostrar error
                        }
                    }
                },
                isLoading = isLoading,
                modifier = Modifier.fillMaxSize()
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun HeaderSection(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color.White.copy(alpha = 0.1f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Title
        Text(
            text = "Registrar Rostro",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}
