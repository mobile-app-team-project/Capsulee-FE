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
import androidx.compose.ui.graphics.drawscope.Stroke
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
fun LoginScreen(
    navController: NavController
) {
    var userId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
            title = "LOG IN",
            onBackClick = { navController.popBackStack() }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            Box {
                Text(
                    text = "Re:Memory",
                    fontSize = 48.sp,
                    fontFamily = IndieFlowerFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = PurplePrimary,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        drawStyle = Stroke(width = 5f)
                    )
                )
                Text(
                    text = "Re:Memory",
                    fontSize = 48.sp,
                    fontFamily = IndieFlowerFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = PurplePrimary,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_capsulee_main),
                contentDescription = "Capsule Icon",
                modifier = Modifier.size(150.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

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

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = "LOG IN",
                onClick = {
                    navController.navigate("home") {
                        popUpTo("onboarding") { inclusive = true }
                    }
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
                append("Don't have an account? ")
                withStyle(style = SpanStyle(color = PurplePrimary)) {
                    append("Sign up")
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
                            navController.navigate("signup")
                        })
                    }
            )

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun LoginScreenPreview() {
    ReMemoryTheme {
        LoginScreen(navController = rememberNavController())
    }
}