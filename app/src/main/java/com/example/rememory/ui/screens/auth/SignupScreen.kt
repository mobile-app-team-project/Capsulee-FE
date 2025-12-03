package com.example.rememory.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rememory.R
import com.example.rememory.ui.components.AppTextField
import com.example.rememory.ui.components.PrimaryButton
import com.example.rememory.ui.components.SimpleHeader
import com.example.rememory.ui.theme.*

@Composable
fun SignUpScreen(
    navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Spacer(modifier = Modifier.statusBarsPadding())

        SimpleHeader(
            title = "SIGN UP",
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(19.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_capsulee_main),
                contentDescription = "Capsule Icon",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Username 필드
            AppTextField(
                value = username,
                onValueChange = { username = it },
                modifier = Modifier.fillMaxWidth(),
                textFieldModifier = Modifier.height(54.dp),
                label = "Username",
                labelStyle = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = BlackText
                ),
                placeholderText = "This will be shown to others",
                placeholderStyle = TextStyle(
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = GrayText
                ),
                shape = RoundedCornerShape(15.dp),
                defaultBorderColor = PurplePrimary
            )

            Spacer(modifier = Modifier.height(26.dp))

            // ID 필드
            AppTextField(
                value = userId,
                onValueChange = { userId = it },
                modifier = Modifier.fillMaxWidth(),
                textFieldModifier = Modifier.height(54.dp),
                label = "ID",
                labelStyle = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = BlackText
                ),
                placeholderText = "Used for login",
                placeholderStyle = TextStyle(
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = GrayText
                ),
                shape = RoundedCornerShape(15.dp),
                defaultBorderColor = PurplePrimary
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Password 필드
            AppTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                textFieldModifier = Modifier.height(54.dp),
                label = "Password",
                labelStyle = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = BlackText
                ),
                placeholderText = "Must be at least 8 characters",
                placeholderStyle = TextStyle(
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = GrayText
                ),
                shape = RoundedCornerShape(15.dp),
                defaultBorderColor = PurplePrimary
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Confirm Password 필드
            AppTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                modifier = Modifier.fillMaxWidth(),
                textFieldModifier = Modifier.height(54.dp),
                label = "Confirm Password",
                labelStyle = TextStyle(
                    fontSize = 18.sp,
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = BlackText
                ),
                placeholderText = "Enter the same password again",
                placeholderStyle = TextStyle(
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                    color = GrayText
                ),
                shape = RoundedCornerShape(15.dp),
                defaultBorderColor = PurplePrimary
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = "SIGN UP",
                onClick = {
                    // TODO: 회원가입 로직
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(15.dp),
                textStyle = TextStyle(
                    fontFamily = MontserratFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp
                )
            )

            Spacer(modifier = Modifier.height(23.dp))

            val annotatedString = buildAnnotatedString {
                append("Already have an account? ")
                withStyle(style = SpanStyle(color = PurplePrimary)) {
                    append("Log in")
                }
            }

            Text(
                text = annotatedString,
                fontSize = 16.sp,
                fontFamily = MontserratFontFamily,
                fontWeight = FontWeight.Medium,
                color = BlackText,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = {
                            navController.navigate("login")
                        })
                    }
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun SignUpScreenPreview() {
    ReMemoryTheme {
        SignUpScreen(navController = rememberNavController())
    }
}