package com.example.protectify.presentation.visitorsList

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.protectify.domain.visitor.Visitor

@Composable
fun VisitorsListScreen(
    viewModel: VisitorsListViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state

    // Estado para el diálogo de confirmación
    var showDeleteDialog by remember { mutableStateOf(false) }
    var visitorIdToDelete by remember { mutableStateOf<Long?>(null) }

    LaunchedEffect(Unit) {
        viewModel.getVisitors()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF26272C))
    ) {
        HeaderSection(
            onBackClick = {
                viewModel.goToHome()
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            RegisteredPeopleHeader(
                onAddClick = {
                    viewModel.goToAddVisitorScreen()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                state.isLoading -> {
                    LoadingPeopleList()
                }
                else -> {
                    PeopleList(
                        visitors = state.data ?: emptyList(),
                        onPersonClick = { visitorId ->
                            // TODO: Implementar navegación a detalle
                        },
                        onDeleteClick = { visitorId ->
                            visitorIdToDelete = visitorId
                            showDeleteDialog = true
                        }
                    )
                }
            }
        }
    }

    // Diálogo de confirmación, fuera del Column principal
    if (showDeleteDialog && visitorIdToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("¿Eliminar visitante?", color = Color.White) },
            text = { Text("¿Estás seguro de que deseas eliminar este visitante? Esta acción no se puede deshacer.", color = Color.White.copy(alpha = 0.85f)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteVisitor(visitorIdToDelete!!)
                    showDeleteDialog = false
                }) {
                    Text("Eliminar", color = Color(0xFFE17055))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar", color = Color.White)
                }
            },
            containerColor = Color(0xFF26272C)
        )
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

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Rostros",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.size(40.dp))
    }
}

@Composable
private fun RegisteredPeopleHeader(
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Personas Registradas",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        )

        IconButton(
            onClick = onAddClick,
            modifier = Modifier
                .size(32.dp)
                .background(
                    Color.White.copy(alpha = 0.1f),
                    CircleShape
                )
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add person",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
private fun LoadingPeopleList() {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(3) {
            VisitorCardLoading()
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.White
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun PeopleList(
    visitors: List<Visitor>,
    onPersonClick: (Long) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    Log.d("PeopleList", "Cantidad de visitantes: ${visitors.size}")
    if (visitors.isEmpty()) {
        EmptyState()
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(visitors) { visitor ->
                VisitorCard(
                    visitor = visitor,
                    onClick = onPersonClick,
                    onDeleteClick = onDeleteClick
                )
            }
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Face,
                contentDescription = "No people",
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No hay personas registradas",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Toca el botón + para agregar una persona",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Gray.copy(alpha = 0.7f)
                ),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}