package com.example.com_us.ui.compose

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.com_us.ui.question.QuestionViewModel

@Composable
fun AnswerOptionList(answerList: List<String>, questionViewModel: QuestionViewModel) {
    var columCount = 2
    var selectedOption by remember { mutableIntStateOf(-1) } // 선택된 아이템의 ID를 저장하는 상태

    LazyVerticalGrid(
        columns = GridCells.Fixed(columCount)
    ) {
        items(answerList.size) { item ->
            AnswerOptionItem(
                optionId = item,
                text = answerList[item],
                isSelected = selectedOption == item,
                onClick = { selectedOption = it; questionViewModel.updateSelectedAnswerOptionId(it); }
            )
        }
    }
}