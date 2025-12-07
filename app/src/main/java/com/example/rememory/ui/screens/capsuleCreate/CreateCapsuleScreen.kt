import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rememory.ui.components.AppHeader
import com.example.rememory.ui.components.ProgressBar
import com.example.rememory.ui.components.TitleLogoStyle
import com.example.rememory.ui.screens.capsuleCreate.components.BottomButtons
import com.example.rememory.ui.theme.BackgroundLight
import com.example.rememory.ui.theme.BlackText
import com.example.rememory.ui.theme.GrayText

@Composable
fun CreateCapsuleScreen(
    title: String,
    subtitle: String,
    progress: Float,
    showPrevious: Boolean = true,
    previousText: String = "PREVIOUS",
    nextText: String = "NEXT",
    onPrevious: (() -> Unit)? = null,
    onNext: () -> Unit,
    rightEnabled: Boolean = true,
    isSingleButton: Boolean = false,
    showBottomBar: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold (
        topBar = {
            AppHeader(
                title = "Re:Memory",
                titleStyle = TitleLogoStyle,
                onBellClick = {},
            )
        },
        bottomBar = {
            if (showBottomBar) {
                BottomButtons(
                    showPrevious = showPrevious,
                    previousText = previousText,
                    nextText = nextText,
                    onPrevious = onPrevious,
                    onNext = onNext,
                    isSingleButton = isSingleButton,
                    rightEnabled = rightEnabled
                )
            }
        },
        containerColor = BackgroundLight
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 18.dp, vertical = 12.dp) // 여기에 패딩 적용
        ) {
            ProgressBar(progress = progress)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = BlackText,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = GrayText,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(28.dp))

            content()
        }
    }
}


@Preview()
@Composable
fun CreateCapsuleScreen_DoubleButton() {
    CreateCapsuleScreen(
        title = "Create your Memory",
        subtitle = "This will be sealed for you, or your friends",
        progress = 0.25f,
        showPrevious = true,
        previousText = "PREVIOUS",
        nextText = "NEXT",
        onPrevious = {},
        onNext = {},
        rightEnabled = true,
        isSingleButton = false,
        content = {}
    )
}

@Preview()
@Composable
fun CreateCapsuleScreen_SingleButton() {
    CreateCapsuleScreen(
        title = "Seal your Memory",
        subtitle = "Once sealed, you can’t edit it!",
        progress = 1.0f,
        showPrevious = false,
        nextText = "SEAL NOW",
        onNext = {},
        rightEnabled = true,
        isSingleButton = true,
        content = {}
    )
}

@Preview()
@Composable
fun CreateCapsuleScreen_Disabled() {
    CreateCapsuleScreen(
        title = "Choose Conditions",
        subtitle = "Set rules to open this capsule",
        progress = 0.75f,
        showPrevious = true,
        previousText = "BACK",
        nextText = "NEXT",
        onPrevious = {},
        onNext = {},
        rightEnabled = false,
        isSingleButton = false,
        content = {}
    )
}