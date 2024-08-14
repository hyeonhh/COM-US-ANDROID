package com.example.com_us.ui.question

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.example.com_us.data.response.question.ResponseQuestionDto
import com.example.com_us.ui.ColorType
import com.example.com_us.ui.home.AnswerTypeTag
import com.example.com_us.ui.theme.Gray200
import com.example.com_us.ui.theme.Orange700
import com.example.com_us.ui.theme.TextBlack
import com.example.com_us.ui.theme.Typography

@Composable
fun QuestionListItem(data: ResponseQuestionDto) {
    var color = if(data.answerType != "선택형") ColorType.SALMON else ColorType.BLUE

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(0.dp, 5.dp),
        border = BorderStroke(width = 1.dp, color = Gray200),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 30.dp, top = 16.dp, end = 20.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = data.category,
                        style = Typography.titleMedium,
                        color = Orange700,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = "대화 ${data.questionCount}회 완료",
                        style = Typography.labelSmall,
                        color = Color.Gray)
                }
                Text(
                    text = data.questionContent,
                    style = Typography.bodyMedium,
                    color = TextBlack,
                    softWrap = true,
                    modifier = Modifier.padding(top = 4.dp, end = 10.dp)
                )
            }
            AnswerTypeTag(
                colorType = color,
                text = data.answerType)
        }
    }
}