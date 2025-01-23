package com.example.com_us.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.com_us.R
import com.example.com_us.data.model.question.response.question.ResponseQuestionDto
import com.example.com_us.util.ColorMatch
import com.example.com_us.util.ColorType
import com.example.com_us.ui.compose.theme.Gray200
import com.example.com_us.ui.compose.theme.Gray400
import com.example.com_us.ui.compose.theme.TextBlack
import com.example.com_us.ui.compose.theme.Typography

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QuestionListItem(data: ResponseQuestionDto, onClick: () -> Unit) {
    var isLiked by remember { mutableStateOf(false) }
    val color = ColorMatch.fromKor(data.answerType)?.colorType ?: ColorType.GRAY

    Surface(
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(0.dp, 5.dp)
            .drawBehind {
                drawLine(
                    color = Gray400,
                    start = Offset(8.dp.toPx(), size.height), // 시작점
                    end = Offset(size.width-8.dp.toPx(), size.height), // 끝점
                    strokeWidth = 1.dp.toPx() // 두께

                )
            },
        onClick = onClick,
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(start = 20.dp, top = 16.dp, end = 10.dp, bottom = 16.dp),
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
                        style = Typography.headlineMedium,
                        color = ColorMatch.fromKor(data.category)?.color ?: Color.LightGray,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(text = "대화 ${data.questionCount}회 완료",
                        style = Typography.labelMedium,
                        color = Color.Gray)
                    AnswerTypeTag(
                        colorType = color,
                        text = data.answerType)
                }

                    Text(
                        text = data.questionContent,
                        style = Typography.bodyMedium,
                        color = TextBlack,
                        softWrap = true,
                        modifier = Modifier.padding(top = 4.dp, end = 10.dp)
                    )
            }

           IconButton(
                onClick = {
                    isLiked = !isLiked
                }
            ) {
                Icon(
                    modifier = Modifier.width(50.dp),
                    imageVector = if (isLiked )Icons.Filled.Favorite
                    else Icons.Outlined.FavoriteBorder,
                    contentDescription = "heart",
                    tint = if (isLiked) colorResource(R.color.orange_700)
                    else   colorResource(R.color.gray_200)

                )
            }
        }
    }
}
