import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.rememory.ui.components.AppTextField
import com.example.rememory.ui.theme.BlackText
import com.example.rememory.ui.theme.GrayText
import com.example.rememory.R
import java.io.File

@Composable
fun Step1BasicInfo(
    title: String,
    message: String,
    imageFile: File?,
    onTitleChange: (String) -> Unit,
    onMessageChange: (String) -> Unit,
    onImageSelected: () -> Unit
) {
    val borderColor = Color(0xFFE5E7EB)
    val borderStrokeWidth = 1.5.dp

    Column {
        AppTextField(
            value = title,
            onValueChange = onTitleChange,
            label = "Capsule Title",
            placeholderText = "ex. Our Graduation Day",
            modifier = Modifier.fillMaxWidth(),
            textFieldModifier = Modifier.height(45.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        AppTextField(
            value = message,
            onValueChange = onMessageChange,
            label = "Your Message",
            placeholderText = "Remember this moment...",
            counterText = "${message.length}/500",
            isSingleLine = false,
            modifier = Modifier.fillMaxWidth(),
            textFieldModifier = Modifier.height(155.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Photos", style = MaterialTheme.typography.titleSmall, color = BlackText)
        Text(
            "Add moments you want to keep",
            style = MaterialTheme.typography.bodyMedium,
            color = GrayText
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .drawBehind {
                    val stroke = Stroke(
                        width = borderStrokeWidth.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )
                    drawRoundRect(
                        color = borderColor,
                        size = size,
                        style = stroke,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(10.dp.toPx())
                    )
                }
                .clickable { onImageSelected() },
            contentAlignment = Alignment.Center
        ) {
            if (imageFile == null) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_photo),
                        contentDescription = "Add Photo",
                        tint = GrayText,
                        modifier = Modifier.size(36.dp)
                    )
                    Text("Tap to Add", color = GrayText, fontSize = 13.sp)
                }
            } else {
                AsyncImage(
                    model = imageFile,
                    contentDescription = "Selected Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}