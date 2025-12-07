package com.example.rememory.ui.screens.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.rememory.R
import com.example.rememory.ui.components.PrimaryButton
import com.example.rememory.ui.components.SecondaryButton
import com.example.rememory.ui.theme.BackgroundLight
import com.example.rememory.ui.theme.BlackText
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.ui.theme.IndieFlowerFontFamily
import com.example.rememory.ui.theme.MontserratFontFamily
import com.example.rememory.ui.theme.PurplePrimary
import com.example.rememory.ui.theme.ReMemoryTheme

@Composable
fun OnboardingScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 27.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

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
                ),
                modifier = Modifier
                    .width(207.dp)
                    .height(70.dp)
            )
            Text(
                text = "Re:Memory",
                fontSize = 48.sp,
                fontFamily = IndieFlowerFontFamily,
                fontWeight = FontWeight.Normal,
                color = PurplePrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(207.dp)
                    .height(70.dp)
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_capsulee_open),
            contentDescription = "Capsule Icon",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "Unlock memories,\nat the right moment.",
            fontSize = 25.sp,
            fontFamily = MontserratFontFamily,
            fontWeight = FontWeight.Medium,
            color = BlackText,
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            modifier = Modifier
                .width(268.dp)
                .height(60.dp)
        )

        Spacer(modifier = Modifier.height(11.dp))

        Text(
            text = "Create digital capsules\nthat open only when the time is right.",
            fontSize = 16.sp,
            fontFamily = MontserratFontFamily,
            fontWeight = FontWeight.Normal,
            color = GrayText,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier
                .width(302.dp)
                .height(40.dp)
        )

        Spacer(modifier = Modifier.height(69.dp))

        SecondaryButton(
            text = "Sign Up",
            onClick = {
                navController.navigate("signup")
            },
            modifier = Modifier
                .width(216.dp)
                .height(58.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        PrimaryButton(
            text = "Log In",
            onClick = {
                navController.navigate("login")
            },
            modifier = Modifier
                .width(216.dp)
                .height(58.dp),
            shape = RoundedCornerShape(28.dp),
            textStyle = TextStyle(
                fontFamily = MontserratFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp
            )
        )

        Spacer(modifier = Modifier.height(57.dp))
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun OnboardingScreenPreview() {
    ReMemoryTheme {
        OnboardingScreen(navController = rememberNavController())
    }
}