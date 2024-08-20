package com.example.com_us.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.com_us.ui.compose.theme.Gray500
import com.example.com_us.ui.compose.theme.Orange700
import com.example.com_us.ui.compose.theme.Typography

@Composable
fun AnswerOptionItem(optionId: Int, text: String, isSelected: Boolean, onClick: (Int) -> Unit) {

    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 5.dp, end = 5.dp, bottom = 15.dp)
            .border(width = 1.dp, color = if(isSelected) Orange700 else Gray500, shape = RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable { onClick(optionId) },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text,
            Modifier.padding(0.dp, 14.dp), color = if(isSelected) Orange700 else Gray500, style = Typography.titleLarge)
    }
}