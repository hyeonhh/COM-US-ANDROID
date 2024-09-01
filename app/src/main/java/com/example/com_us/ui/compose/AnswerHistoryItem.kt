package com.example.com_us.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.sp
import com.example.com_us.ui.compose.theme.Gray100
import com.example.com_us.ui.compose.theme.Gray400
import com.example.com_us.ui.compose.theme.TextBlack
import com.example.com_us.ui.compose.theme.Typography

@Composable
fun AnswerHistoryItem(date: String, answer: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 7.5.dp, top = 0.dp, end = 7.5.dp, bottom = 15.dp)
            .border(width = 1.dp, color = Gray100, shape = RoundedCornerShape(10.dp))
            .background(Color.White),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = date,
            Modifier.padding(start = 15.dp, top = 15.dp, end = 15.dp, bottom = 4.dp), color = Gray400, style = Typography.titleSmall)
        Text(text = answer,
            Modifier.padding(start = 15.dp, end = 15.dp, bottom = 15.dp), color = TextBlack, style = Typography.headlineLarge, fontSize = 16.sp)
    }
}