package com.example.com_us.util

import androidx.compose.ui.res.stringResource
import com.example.com_us.R

enum class ThemeType(val kor: String, val blockRes: Int?) {
    ALL("전체", null),
    DAILY("일상", R.drawable.img_block_char_orange),
    SCHOOL("학교", R.drawable.img_block_char_blue),
    FRIEND("친구", R.drawable.img_block_char_purple),
    FAMILY("가족", R.drawable.img_block_char_salmon),
    HOBBY("관심사", R.drawable.img_block_char_green),
    RANDOM("랜덤", null);

    companion object {
        // kor 값으로 ThemeType을 찾는 메서드
        fun fromKor(kor: String): ThemeType? {
            return values().find { it.kor == kor }
        }

        fun findResFromKor(kor: String): Int? {
            return values().find { it.kor == kor }?.blockRes
        }
    }
}