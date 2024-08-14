package com.example.com_us.ui

enum class ThemeType(val kor: String) {
    ALL("전체"),
    DAILY("일상"),
    SCHOOL("학교"),
    FRIEND("친구"),
    FAMILY("가족"),
    HOBBY("관심사"),
    RANDOM("랜덤");

    companion object {
        // kor 값으로 ThemeType을 찾는 메서드
        fun fromKor(kor: String): ThemeType? {
            return values().find { it.kor == kor }
        }
    }
}