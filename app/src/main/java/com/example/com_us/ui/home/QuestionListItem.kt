package com.example.com_us.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.com_us.ui.ColorType
import com.example.com_us.ui.theme.Gray200
import com.example.com_us.ui.theme.Orange700
import com.example.com_us.ui.theme.TextBlack
import com.example.com_us.ui.theme.Typography

@Preview(showBackground = true)
@Composable
fun QuestionListItem() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(width = 10.dp, color = Gray200, shape = RoundedCornerShape(10.dp))
            .padding(start = 30.dp, top = 15.dp, end = 20.dp, bottom = 15.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = "일상", style = Typography.titleMedium, color = Orange700, modifier = Modifier.padding(end = 4.dp))
                    Text(text = "대화 3회 완료", style = Typography.labelSmall, color= Color.Gray)
                }
                Text(text = "오늘 하루 기분은 어땠어?", style = Typography.bodyMedium, color = TextBlack, modifier = Modifier.padding(top = 4.dp))
            }
            AnswerTypeTag(colorType = ColorType.BLUE, text = "선택형")
        }
    }
}