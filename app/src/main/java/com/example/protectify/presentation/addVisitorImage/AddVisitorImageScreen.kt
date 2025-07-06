package com.example.protectify.presentation.addVisitorImage

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun AddVisitorImageScreen(
    viewModel: AddVisitorImageViewModel,
    modifier: Modifier = Modifier
) {
    val photoUrl by viewModel.photoUrl
    val state by viewModel.state

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.uploadImage(it) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF26272C))
    ) {
        // Header
        HeaderSection(
            onBackClick = {
                viewModel.goToAddVisitorScreen()
            }
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Title and Description
            TitleSection()

            Spacer(modifier = Modifier.height(48.dp))

            // Profile Image Section
            ProfileImageSection(
                photoUrl = photoUrl,
                isLoading = state.isLoading,
                onImageClick = {
                    imagePickerLauncher.launch("image/*")
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Continue Button
            ContinueButton(
                enabled = photoUrl.isNotBlank() && !state.isLoading,
                isLoading = state.isLoading,
                onClick = {
                    viewModel.createVisitor()
                }
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
            text = "Foto de Perfil",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}

@Composable
private fun TitleSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Seleccione una foto de perfil para el contacto",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        )
    }
}

@Composable
private fun ProfileImageSection(
    photoUrl: String,
    isLoading: Boolean,
    onImageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .clickable { onImageClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            // Loading state
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.3f),
                        CircleShape
                    )
                    .border(3.dp, Color.White.copy(alpha = 0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFE17055),
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(40.dp)
                )
            }
        } else if (photoUrl.isNotBlank()) {
            // Image loaded
            GlideImage(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .border(3.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                imageModel = { photoUrl },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                ),
                loading = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Gray.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFFE17055),
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                failure = {
                    PlaceholderImage()
                }
            )
        } else {
            // Placeholder state
            PlaceholderImage()
        }

        // Camera icon overlay
        if (!isLoading) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-16).dp, y = (-16).dp)
                    .size(48.dp)
                    .background(Color(0xFFE17055), CircleShape)
                    .border(3.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.List,
                    contentDescription = "Select photo",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun PlaceholderImage() {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(
                Color.Gray.copy(alpha = 0.3f),
                CircleShape
            )
            .border(3.dp, Color.White.copy(alpha = 0.3f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Person,
                contentDescription = "Add photo",
                tint = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Toca para agregar foto",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.White.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center
                )
            )
        }
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
                    text = "Creando visitante...",
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

@Composable
private fun ErrorMessage(
    message: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Red.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Warning,
                contentDescription = "Error",
                tint = Color.Red,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color.Red
                )
            )
        }
    }
}
