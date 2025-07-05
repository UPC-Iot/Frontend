package com.example.protectify.presentation.shared

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.protectify.R

@Composable
fun StartInfoScreen(padding: PaddingValues){
    val darkBackground = Color(0xFF26272C)
    val orangeButton = Color(0xFFBF4D36)
    val grayText = Color(0xFF8E8E93)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .padding(padding)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id= R.drawable.face_recon),
            contentDescription = "Info Image",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Seguridad inteligente, rostro por rostro",
            fontSize = 32.sp,
            lineHeight = 40.sp,
            modifier = Modifier.width(280.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Detecta y reconoce quién entra a tu hogar",
            fontSize = 18.sp,
            lineHeight = 24.sp,
            modifier = Modifier.width(300.dp),
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            color = grayText
        )

        Spacer(modifier = Modifier.height(72.dp))

        Button(
            onClick = { /* Save profile logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = orangeButton)
        ) {
            Text(
                text = "Continue",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}