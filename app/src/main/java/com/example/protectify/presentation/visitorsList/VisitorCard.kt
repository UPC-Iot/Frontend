package com.example.protectify.presentation.visitorsList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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
import com.example.protectify.domain.visitor.Visitor
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun VisitorCard(
    visitor: Visitor,
    onClick: ((Long) -> Unit)? = null,
    onDeleteClick: ((Long) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null && visitor.id != null) {
                    Modifier.clickable { onClick(visitor.id) }
                } else Modifier
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        VisitorAvatar(
            photoUrl = visitor.photo,
            contentDescription = "${visitor.firstname} ${visitor.lastname}"
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Name and role
        VisitorInfo(
            firstname = visitor.firstname,
            lastname = visitor.lastname,
            role = visitor.role,
            modifier = Modifier.weight(1f)
        )

        // Delete button
        if (onDeleteClick != null && visitor.id != null) {
            DeleteButton(
                onClick = { onDeleteClick(visitor.id) }
            )
        }
    }
}

@Composable
private fun VisitorAvatar(
    photoUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    GlideImage(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape),
        imageModel = { photoUrl.ifBlank { android.R.drawable.ic_menu_gallery } },
        imageOptions = ImageOptions(
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        ),
        loading = {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Gray.copy(alpha = 0.3f), CircleShape)
            )
        },
        failure = {
            Image(
                painter = painterResource(id = android.R.drawable.ic_menu_gallery),
                contentDescription = contentDescription,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
        }
    )
}

@Composable
private fun VisitorInfo(
    firstname: String,
    lastname: String,
    role: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "$firstname $lastname".trim(),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        )
        Text(
            text = role,
            style = MaterialTheme.typography.bodySmall.copy(
                color = Color.Gray
            )
        )
    }
}

@Composable
private fun DeleteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(32.dp)
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "Delete person",
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun VisitorCardLoading(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar placeholder
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    Color.Gray.copy(alpha = 0.3f),
                    CircleShape
                )
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Text placeholders
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(16.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.3f),
                        RoundedCornerShape(8.dp)
                    )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(12.dp)
                    .background(
                        Color.Gray.copy(alpha = 0.2f),
                        RoundedCornerShape(6.dp)
                    )
            )
        }

        // Delete button placeholder
        Box(
            modifier = Modifier
                .size(24.dp)
                .background(
                    Color.Gray.copy(alpha = 0.3f),
                    RoundedCornerShape(4.dp)
                )
        )
    }
}

// Preview para testing
@Composable
fun VisitorCardPreview() {
    val sampleVisitor = Visitor(
        id = 1L,
        firstname = "Yamilet",
        lastname = "Rodriguez",
        photo = "",
        role = "Yo",
        lastVisit = null,
        houseId = 1L
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1A1A1A))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Normal card
        VisitorCard(
            visitor = sampleVisitor,
            onClick = { },
            onDeleteClick = { }
        )

        // Loading card
        VisitorCardLoading()

        // Card without delete button
        VisitorCard(
            visitor = sampleVisitor.copy(firstname = "Alexa", role = "Mama"),
            onClick = { }
        )
    }
}
