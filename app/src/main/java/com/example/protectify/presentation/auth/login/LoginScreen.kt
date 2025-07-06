package com.example.protectify.presentation.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.protectify.common.Routes

@Composable
fun LoginScreen(padding: PaddingValues, navController: NavController) {


    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val grayText = Color(0xFF8E8E93)

    val darkBackground = Color(0xFF26272C)
    val orangeButton = Color(0xFFBF4D36)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .padding(padding)
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Box(
            modifier = Modifier
                .padding(bottom = 48.dp, top = 12.dp)
                .size(40.dp)
                .background(color = Color.Black, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = { navController.popBackStack()},
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.Black)
            ){
                Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White
            )}

        }
        Text(
            text = "Iniciar Sesión",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Form Fields
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column {
                Text(
                    text = "Correo",
                    color = grayText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = grayText,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    BasicTextField(
                        value = email,
                        onValueChange = { email = it },
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White,
                            fontSize = 16.sp
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (email.isEmpty()) {
                                Text("Ingresa tu correo", color = Color.Gray)
                            }
                            innerTextField()
                        }
                    )

                }

                Divider(
                    color = Color.DarkGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Password
            Column {
                Text(
                    text = "Contraseña",
                    color = grayText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = null,
                        tint = grayText,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    BasicTextField(
                        value = password,
                        onValueChange = { password = it },
                        textStyle = LocalTextStyle.current.copy(
                            color = Color.White,
                            fontSize = 16.sp
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        decorationBox = { innerTextField ->
                            Box {
                                if (password.isEmpty()) {
                                    Text("Ingresa tu contraseña", color = Color.Gray)
                                }
                                innerTextField()
                            }
                        }
                    )
                }

                Divider(
                    color = Color.DarkGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }


        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = { /* Save profile logic */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = orangeButton)
        ) {
            Text(
                text = "Sign In",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "¿No tienes cuenta? ",
                color = grayText
            )
            Text(
                text = "Regístrate",
                color = Color(0xFF0859d5),
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Register.route)
                }
            )
        }

    }
}