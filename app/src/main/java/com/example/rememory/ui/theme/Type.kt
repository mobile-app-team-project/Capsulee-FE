package com.example.rememory.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.rememory.R

val MontserratFontFamily = FontFamily(
    Font(R.font.montserrat_light, FontWeight.Light),
    Font(R.font.montserrat_regular, FontWeight.Normal),
    Font(R.font.montserrat_medium, FontWeight.Medium),
    Font(R.font.montserrat_semibold, FontWeight.SemiBold),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

val IndieFlowerFontFamily = FontFamily(
    Font(R.font.indie_flower, FontWeight.Normal)
)

// Set of Material typography styles to start with
val Typography = Typography(
    // 헤드라인
    displayLarge = TextStyle( // 메인 화면 제목, 큰 제목
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 25.sp,
        lineHeight = 30.sp
    ),
    displayMedium = TextStyle( // 섹션 제목
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    displaySmall = TextStyle( // 카드 제목
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 18.sp
    ),

    // 타이틀
    titleLarge = TextStyle( // 각 화면 상단 제목
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        lineHeight = 27.sp
    ),
    titleMedium = TextStyle( // 캡슐 이름, 리스트 아이템 제목
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 22.sp
    ),
    titleSmall = TextStyle( // 서브 타이틀
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),

    // 본문
    bodyLarge = TextStyle( // 중요한 설명
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 18.sp
    ),
    bodyMedium = TextStyle( // 대부분의 설명 텍스트
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 16.sp
    ),
    bodySmall = TextStyle( // 부가 정보, 힌트
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),

    // 레이블 (버튼 등)
    labelLarge = TextStyle( // Primary 버튼
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 29.sp
    ),
    labelMedium = TextStyle( // Secondary 버튼, 탭
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp
    ),
    labelSmall = TextStyle( // 태그, 상태 표시
        fontFamily = MontserratFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 17.sp
    )
)