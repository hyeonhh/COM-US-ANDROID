package com.example.com_us.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.com_us.data.response.question.ResponseQuestionDto
import com.example.com_us.util.ColorMatch
import com.example.com_us.util.ColorType
import com.example.com_us.ui.compose.theme.Gray200
import com.example.com_us.ui.compose.theme.TextBlack
import com.example.com_us.ui.compose.theme.Typography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuestionListItem(data: ResponseQuestionDto, onClick: () -> Unit) {

    var color = ColorMatch.fromKor(data.answerType)?.colorType ?: ColorType.GRAY


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(0.dp, 5.dp),
        onClick = onClick,
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
                        color = ColorMatch.fromKor(data.category)?.color ?: Color.LightGray,
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