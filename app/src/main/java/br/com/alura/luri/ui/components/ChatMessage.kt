package br.com.alura.luri.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import br.com.alura.luri.ui.theme.authorBackgroundColor
import br.com.alura.luri.ui.theme.botBackgroundColor
import br.com.alura.luri.ui.theme.ChatTextColor

@Composable
fun ChatMessage(text: String, isAuthor: Boolean = false) {
    val (boxAlignment, boxColor) = if (isAuthor) {
        Pair(Alignment.CenterStart, authorBackgroundColor)
    } else {
        Pair(Alignment.CenterEnd, botBackgroundColor)
    }
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(8.dp)
                .widthIn(
                    max = maxWidth / 1.3f
                )
                .align(boxAlignment)
                .background(
                    boxColor,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Text(
                text = text,
                Modifier.padding(8.dp),
                style = TextStyle.Default.copy(
                    color = ChatTextColor
                )
            )
        }
    }

}
