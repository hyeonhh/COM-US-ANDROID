package com.example.com_us.util

import androidx.compose.ui.res.stringResource
import com.example.com_us.R

enum class ThemeType(val kor: String, val blockRes: Int?) {
    ALL("전체", null),
    DAILY("DAILY", R.drawable.img_block_char_orange),
    SCHOOL("SCHOOL", R.drawable.img_block_char_blue),
    FRIEND("FRIEND", R.drawable.img_block_char_purple),
    FAMILY("FAMILY", R.drawable.img_block_char_salmon),
    HOBBY("HOBBY", R.drawable.img_block_char_green),
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