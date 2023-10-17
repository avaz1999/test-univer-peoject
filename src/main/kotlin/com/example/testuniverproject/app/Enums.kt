package com.example.testuniverproject.app

enum class UserRole {
    DEV, ADMIN, CLIENT
}


enum class ErrorCode(val code: Int){
    QUESTION_NOT_FOUND(100),
    ANSWER_IS_NOT_EMPTY(101)

}