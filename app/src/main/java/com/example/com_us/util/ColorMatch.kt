package com.example.com_us.util

import androidx.compose.ui.graphics.Color
import com.example.com_us.ui.compose.theme.Blue700
import com.example.com_us.ui.compose.theme.Green700
import com.example.com_us.ui.compose.theme.Orange700
import com.example.com_us.ui.compose.theme.Pink700
import com.example.com_us.ui.compose.theme.Purple700
import com.example.com_us.ui.compose.theme.Salmon700

enum class ColorMatch(val kor: String, val colorType: ColorType, val color: Color) {
    DAILY("일상", ColorType.ORANGE, Orange700),
    SCHOOL("학교", ColorType.BLUE, Blue700),
    FRIEND("친구", ColorType.GREEN, Green700),
    FAMILY("가족", ColorType.PURPLE, Purple700),
    HOBBY("관심사", ColorType.SALMON, Salmon700),
    RANDOM("랜덤", ColorType.PINK, Pink700),
    INTERACTIVE("대화형", ColorType.SALMON, Salmon700),
    OPTIONAL("선택형", ColorType.BLUE, Blue700);

    companion object {
        // kor 값으로 ThemeType을 찾는 메서드
        fun fromKor(kor: String): ColorMatch? {
            return values().find { it.kor == kor }
        }
    }
}