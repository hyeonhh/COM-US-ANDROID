package com.example.com_us.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.com_us.R
import com.example.com_us.data.model.question.Answer
import com.example.com_us.data.model.question.Data
import com.example.com_us.ui.record.month.RecordFragmentDirections
import com.example.com_us.ui.record.month.RecordFragmentDirections.Companion.actionRecordFragmentToLoadingFragment


@Composable
fun TimeLineList(items : List<Answer>,
                 answerDate : String,
                 navController : NavController,
                 directions : RecordFragmentDirections.Companion,
                 ) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            items.forEachIndexed { index, answer ->
                if (items.size ==  index+1 ) {
                    TimeLineItem(answer, true, onClick = {
                        val action = directions.actionRecordFragmentToLoadingFragment(
                            isRecord = true,
                            answerDate = answerDate,
                            answer = answer.answerContent,
                            question = answer.questionContent,
                            category = answer.category,
                            answerType = answer.answerType
                        )
                        navController.navigate(action)
                    })
                }else {
                    TimeLineItem(answer, false,onClick = {
                        val action = directions.actionRecordFragmentToLoadingFragment(
                            isRecord = true,
                            answerDate = answer.answerTime,
                            answer = answer.answerContent,
                            question = answer.questionContent,
                            category = answer.category,
                            answerType = answer.answerType
                        )
                        navController.navigate(action)
                    })
                }
            }
        }
    }
}

@Composable
fun TimeLineItem(item : Answer,isLastItem : Boolean,onClick : () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        // 타임라인 왼쪽 부분 (시간 및 라인)
        TimelineLeft(time = item.answerTime, isLastItem = isLastItem, category = item.category)

        // 타임라인 오른쪽 부분 (콘텐츠)
        TimelineContent(item,onClick)
    }
}

@Composable
fun TimelineLeft(time: String, isLastItem: Boolean, category : String) {
    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row {
                // 원형 포인트
                val color = when (category) {
                    "DAILY" -> Color(0xFFFF9D0A)  // orange_700
                    "SCHOOL" -> Color(0xFF00A3E8)  // blue_700
                    "HOBBY" -> Color(0xFFFF7474)   // salmon_700
                    "FRIEND" -> Color(0xFF00BE08)  // purple_700
                    "FAMILY" -> Color(0xFF9295FF)  // green_700

                    else -> Color(0xFFFF9D0A)      // 기본값은 orange_700
                }
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }

            // 연결선 (마지막 아이템이 아닌 경우)
            if (!isLastItem) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(120.dp)
                        .background(Color.LightGray)


                )
            }

        }
        // 시간 텍스트
        Text(
            modifier = Modifier.padding(
                start = 10.dp
            ),
            text = time,
            fontSize = 16.sp,
            color = Color.Gray,
        )
    }
}

@Composable
fun TimelineContent(item: Answer,onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(start = 12.dp, bottom = 24.dp)
    ) {

        // 카테고리 및 타입 행
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)

        ) {
            // 카테고리 텍스트
           val category  =  when(item.category) {
                "DAILY" -> "일상"
                "SCHOOL" -> "학교"
                "HOBBY" -> "취미"
                "FRIEND" -> "친구"
                "FAMILY" -> "가족"
                else -> ""
            }
            Text(
                text = category,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color  = when (item.category) {
                    "DAILY" -> Color(0xFFFF9D0A)  // orange_700
                    "SCHOOL" -> Color(0xFF00A3E8)  // blue_700
                    "HOBBY" -> Color(0xFFFF7474)   // salmon_700
                    "FRIEND" -> Color(0xFF00BE08)  //green_700
                    "FAMILY" -> Color(0xFF9295FF)  //  purple_700
                    else -> Color.Black      // 기본값은 orange_700
                },
                modifier = Modifier.padding(end = 8.dp)
            )

            // 카테고리 타입 표시
            val typeText = if(item.answerType=="MULTIPLE_CHOICE") "선택형" else "대화형"
            val typeColor = if (item.answerType == "MULTIPLE_CHOICE") Color(0xFF4A90E2) else Color(0xFFFF7474)
            val backgroundColor =  if (item.answerType == "MULTIPLE_CHOICE") Color(0xFFEDFFFF) else Color(0xFFFFF0F8)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(2.43.dp)) // 박스에 둥근 모서리 적용
                    .border(
                        width = 1.dp,
                        color = typeColor,
                        shape = RoundedCornerShape(2.43.dp) // 테두리에도 동일한 둥근 모서리 적용
                    )
                    .background(backgroundColor)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = typeText,
                    fontSize = 12.sp,
                    color = typeColor
                )
            }
        }

        // 메인 콘텐츠
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp, bottom = 8.dp),
            )
        {
            Text(
                text = item.questionContent,
                fontSize = 16.sp,
            )
        }

        // 답변
        Row {
            Text(
                text = "답변: ",
                color = Color.Gray
            )
            Text(
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                text = item.answerContent,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimelinePreview() {
    // 샘플 데이터
    val timelineItems = listOf(
        Answer(
            answerTime = "20:30",
            category = "SCHOOL",
            answerType = "MULTIPLE_CHOICE",
            questionContent = "오늘 학교에서 무슨 일 있었어?",
            answerContent = "학교에서 시험봤어"
        ),
        Answer(
            answerTime = "21:35",
            category = "DAILY",
            answerType = "SENTENCE",
            questionContent = "기분이 안 좋을 때, 기분이 좋아지려면 어떤 걸 하면 좋을까요?",
            answerContent = "친구와 만나서 즐겁게 놀면 기분이 좋아져요. 그리..."
        )
    )
   // TimeLineList(timelineItems,)
}