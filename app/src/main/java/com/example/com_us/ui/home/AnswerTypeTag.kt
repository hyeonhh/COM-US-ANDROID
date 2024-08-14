package com.example.com_us.ui.home

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
import com.example.com_us.ui.ColorType
import com.example.com_us.ui.theme.Blue200
import com.example.com_us.ui.theme.Blue500
import com.example.com_us.ui.theme.Blue700
import com.example.com_us.ui.theme.Orange200
import com.example.com_us.ui.theme.Orange500
import com.example.com_us.ui.theme.Orange700
import com.example.com_us.ui.theme.Salmon200
import com.example.com_us.ui.theme.Salmon500
import com.example.com_us.ui.theme.Salmon700

@Composable
fun AnswerTypeTag(colorType: ColorType, text: String) {

    val colorPalette = getColor(colorType)

    Box(
        Modifier
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