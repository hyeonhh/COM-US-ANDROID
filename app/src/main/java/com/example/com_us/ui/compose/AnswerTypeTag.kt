package com.example.com_us.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.com_us.ui.compose.theme.Blue200
import com.example.com_us.ui.compose.theme.Blue500
import com.example.com_us.ui.compose.theme.Blue700
import com.example.com_us.ui.compose.theme.Gray200
import com.example.com_us.ui.compose.theme.Green200
import com.example.com_us.ui.compose.theme.Green500
import com.example.com_us.ui.compose.theme.Green700
import com.example.com_us.ui.compose.theme.Orange200
import com.example.com_us.ui.compose.theme.Orange500
import com.example.com_us.ui.compose.theme.Orange700
import com.example.com_us.ui.compose.theme.Pink200
import com.example.com_us.ui.compose.theme.Pink500
import com.example.com_us.ui.compose.theme.Pink700
import com.example.com_us.ui.compose.theme.Purple200
import com.example.com_us.ui.compose.theme.Purple500
import com.example.com_us.ui.compose.theme.Purple700
import com.example.com_us.ui.compose.theme.Salmon200
import com.example.com_us.ui.compose.theme.Salmon500
import com.example.com_us.ui.compose.theme.Salmon700
import com.example.com_us.util.ColorType

@Composable
fun AnswerTypeTag(colorType: ColorType, text: String) {

    val colorPalette = getColor(colorType)

    Box(
        Modifier
            .padding(5.dp, 0.dp)
            .border(width = 1.dp, color = colorPalette.border, shape = RoundedCornerShape(4.dp))
            .background(color = colorPalette.background)
    ) {
        Text(text = text,
            softWrap = false,
            modifier = Modifier.padding(10.dp, 5.dp),
            color = colorPalette.text)
    }
}

fun getColor(colorType: ColorType): ColorPalette {
    val colorPalettes = mapOf(
        ColorType.BLUE to ColorPalette(
            background = Blue200,
            border = Blue500,
            text = Blue700
        ),
        ColorType.SALMON to ColorPalette(
            background = Salmon200,
            border = Salmon500,
            text = Salmon700
        ),
        ColorType.ORANGE to ColorPalette(
            background = Orange200,
            border = Orange500,
            text = Orange700
        ),
        ColorType.GREEN to ColorPalette(
            background = Green200,
            border = Green500,
            text = Green700
        ),
        ColorType.PURPLE to ColorPalette(
            background = Purple200,
            border = Purple500,
            text = Purple700
        ),
        ColorType.PINK to ColorPalette(
            background = Pink200,
            border = Pink500,
            text = Pink700
        ),
        ColorType.GRAY to ColorPalette(
            background = Gray200,
            border = Color.LightGray,
            text = Color.Gray
        )
    )

    return colorPalettes[colorType] ?: ColorPalette(
        background = Color.LightGray,
        border = Color.DarkGray,
        text = Color.Black
    )
}

data class ColorPalette(
    val background: Color,
    val border: Color,
    val text: Color
)