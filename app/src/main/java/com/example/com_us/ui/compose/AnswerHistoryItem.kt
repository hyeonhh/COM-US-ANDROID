package com.example.com_us.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.com_us.data.model.question.Answer
import com.example.com_us.data.model.question.response.question.PreviousAnswer
import com.example.com_us.ui.compose.theme.Gray100
import com.example.com_us.ui.compose.theme.Gray400
import com.example.com_us.ui.compose.theme.TextBlack
import com.example.com_us.ui.compose.theme.Typography
import com.example.com_us.ui.question.previous.PreviousAnswerFragmentDirections
import com.example.com_us.util.ColorMatch

// 스크롤 가능한 대화 기록 리스트
@Composable
fun AnswerHistoryList(
    answers: List<PreviousAnswer>,
    direction : PreviousAnswerFragmentDirections.Companion,
    navController: NavController,
    question : String,
    category : String,
    ) {
    Column(
        modifier = Modifier
            .background(Color.White),
    ) {
        for (answer in answers) {
            AnswerHistoryItem(
                date = answer.answerDate,
                answer = answer.answerContent,
                type = answer.answerType,
                onClick = {
                   val action =direction.actionPreviousAnswerFragmentToLoadingFragment(
                       isRecord = true,
                       answerDate = answer.answerDate,
                       answer = answer.answerContent,
                       question = question,
                       answerType = answer.answerType,
                       category = category
                   )
                    navController.navigate(action)

                }
            )
        }
    }
}


@Composable
fun AnswerHistoryItem(
    date: String,
    answer: String,
    type: String, // "선택형" 또는 "대화형"
    onClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 날짜와 대화 유형 행
        Row(
            modifier = Modifier
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = date+"의 대화",
                color = Color.Gray,
                style = Typography.bodyMedium
            )

            Box(
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = if (type == "MULTIPLE_CHOICE") Color(0xFF00A3E8) else Color(
                            0xFFFF7474
                        ),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .background(
                        color = if (type == "MULTIPLE_CHOICE") Color(0xFFEDFFFF) else Color(
                            0xFFFFF0F8
                        )
                    )
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (type == "MULTIPLE_CHOICE") "선택형" else "대화형",
                    color = if (type == "MULTIPLE_CHOICE") Color(0xFF00A3E8) else  Color(0xFFFF7474),
                    style = Typography.bodySmall
                )
            }
        }

        // 대화 내용과 상세보기 행
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = answer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Typography.bodyLarge,
                modifier = Modifier.weight(1f)
            )

            TextButton(
                onClick = onClick,
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "상세보기",
                    color = Color.Gray,
                    style = Typography.bodyMedium
                )
            }
        }

        // 구분선
        Divider(
            color = Color(0xFFEEEEEE),
            thickness = 1.dp,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}