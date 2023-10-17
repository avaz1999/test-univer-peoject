package com.example.testuniverproject.app

import java.util.TimerTask

data class BaseMessage(
    val code: Int,
    val message: String?
) {
    companion object {
        fun created() = BaseMessage(1, "CREATED")
        fun updated() = BaseMessage(1, "UPDATED")
        fun deleted() = BaseMessage(1, "DELETED")
    }
}
data class ValidationErrorMessage(
    val code: Int,
    val fields: Map<String, Any?>,
    val message: String? = "Validation error",
) {

    companion object {
        fun validation(fields: Map<String, Any?>) = ValidationErrorMessage(-2, fields)
    }
}


data class UserDto(
    val firstName: String,
    val lastName: String,
    val username: String,
    val password: String
) {
    companion object {
        fun toDto(user: User): UserDto = user.run {
            UserDto(user.firstName, user.lastName, user.username, user.password)
        }
    }
}

data class QuestionDto(
    val title: String,
    val answer: List<AnswerDto>? = null
) {
    companion object {
        fun toDto(question: Question, answerDtoList: MutableList<AnswerDto>) = question.run {
            QuestionDto(question.title,answerDtoList)
        }

        fun toDtoList(question: Question?) = question.run {
            QuestionDto(question!!.title)
        }
    }
}

data class SessionDto(
    val result: TimerTask
)

data class AnswerDto(
    val variant: String,
    val trueOrFalse: Boolean = false
)