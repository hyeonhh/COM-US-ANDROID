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
import com.example.com_us.ui.theme.Typography

@Composable
fun AnswerOptionItem(text: String) {
    Box(
        Modifier
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
            .background(color = Color.White)
    ) {
        Text(text = text,
            Modifier.padding(62.dp, 14.dp), color = Color.Gray, style = Typography.titleLarge)
    }
}