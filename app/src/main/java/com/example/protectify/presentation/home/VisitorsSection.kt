package com.example.protectify.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.protectify.R
import com.example.protectify.domain.visitor.Visitor
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun VisitorsSection(
    visitors: List<Visitor>,
    isLoading: Boolean,
    onAddVisitorClick: () -> Unit,
    onVisitorClick: ((Long) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFBF4D36) // Color naranja
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Título de la sección
            Text(
                text = "Personas Registradas",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Línea divisoria
            HorizontalDivider(
                color = Color.White.copy(alpha = 0.3f),
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Contenido: Loading o Lista de visitantes
            if (isLoading) {
                LoadingVisitorsContent()
            } else {
                VisitorsContent(
                    visitors = visitors,
                    onAddVisitorClick = onAddVisitorClick,
                    onVisitorClick = onVisitorClick
                )
            }
        }
    }
}

@Composable
private fun LoadingVisitorsContent() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(3) {
            VisitorLoadingItem()
        }
    }
}

@Composable
private fun VisitorLoadingItem() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar placeholder
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    Color.White.copy(alpha = 0.3f),
                    CircleShape
                )
                .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape)
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Name placeholder
        Box(
            modifier = Modifier
                .width(50.dp)
                .height(14.dp)
                .background(
                    Color.White.copy(alpha = 0.3f),
                    RoundedCornerShape(7.dp)
                )
        )
        Spacer(modifier = Modifier.height(4.dp))
        // Role placeholder
        Box(
            modifier = Modifier
                .width(35.dp)
                .height(12.dp)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    RoundedCornerShape(6.dp)
                )
        )
    }
}

@Composable
private fun VisitorsContent(
    visitors: List<Visitor>,
    onAddVisitorClick: () -> Unit,
    onVisitorClick: ((Long) -> Unit)?
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Botón "Add" al inicio
        item {
            AddVisitorCard(onClick = onAddVisitorClick)
        }

        // Lista de visitantes (máximo 5 para no sobrecargar la UI)
        items(visitors.take(5)) { visitor ->
            VisitorCard(
                visitor = visitor,
                onClick = onVisitorClick
            )
        }

        // Indicador de más visitantes si hay más de 5
        if (visitors.size > 5) {
            item {
                MoreVisitorsCard(count = visitors.size - 5)
            }
        }
    }
}

@Composable
private fun AddVisitorCard(
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    Color.White.copy(alpha = 0.3f),
                    CircleShape
                )
                .border(2.dp, Color.White.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Add visitor",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Agregar",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        )
    }
}

@Composable
private fun VisitorCard(
    visitor: Visitor,
    onClick: ((Long) -> Unit)?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.then(
            if (onClick != null && visitor.id != null) {
                Modifier.clickable { onClick(visitor.id) }
            } else Modifier
        )
    ) {
        // Avatar del visitante
        GlideImage(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White.copy(alpha = 0.7f), CircleShape),
            imageModel = { visitor.photo.ifBlank { android.R.drawable.ic_menu_gallery } },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center
            ),
            loading = {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Cargando imagen",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            },
            failure = {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "Imagen no disponible",
                    modifier = Modifier.matchParentSize(),
                    contentScale = ContentScale.Crop
                )
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Nombre del visitante
        Text(
            text = visitor.firstname,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )

        // Rol del visitante
        Text(
            text = visitor.role,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.8f)
            )
        )
    }
}

@Composable
private fun MoreVisitorsCard(
    count: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(
                    Color.White.copy(alpha = 0.2f),
                    CircleShape
                )
                .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "+$count",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "More",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.8f)
            )
        )
    }
}

