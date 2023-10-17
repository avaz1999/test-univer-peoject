package com.example.testuniverproject.app

import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ResourceBundleMessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler(
    private val errorMessageSource: ResourceBundleMessageSource,
) {
    @ExceptionHandler(TestServiceException::class)
    fun handleUserException(exception: TestServiceException): ResponseEntity<BaseMessage> {
        return ResponseEntity.badRequest().body(exception.getErrorMessage(errorMessageSource))
    }

    @ExceptionHandler(BindException::class)
    fun handleValidationException(e: BindException): ResponseEntity<ValidationErrorMessage> {
        val fields = mutableMapOf<String, Any?>()
        e.bindingResult.fieldErrors.forEach { fields[it.field] = it.defaultMessage }
        return ResponseEntity.badRequest().body(ValidationErrorMessage.validation(fields))
    }
//    @ExceptionHandler(RuntimeException::class)
//    fun handleValidationException(e: RuntimeException): ResponseEntity<BaseMessage> {
////        errorBot.sendLog(e)
//        return when (e) {
//            is AuthenticationException -> ResponseEntity.status(401).body(BaseMessage(401, e.message))
//            else -> ResponseEntity.badRequest().body(BaseMessage(400, e.message))
//        }
//    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleValidationException(e: AccessDeniedException): ResponseEntity<BaseMessage> {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BaseMessage(HttpStatus.FORBIDDEN.value(), e.message))
    }

    @ExceptionHandler(Throwable::class)
    fun handleThrowable(e: Throwable): ResponseEntity<BaseMessage> {
        return when (e) {
            is HttpRequestMethodNotSupportedException -> ResponseEntity.status(e.statusCode)
                .body(BaseMessage(e.statusCode.value(), e.message))

            else -> ResponseEntity.badRequest().body(BaseMessage(HttpStatus.BAD_REQUEST.value(), e.message))
        }
    }
}

sealed class TestServiceException() : RuntimeException() {
    abstract fun errorCode(): ErrorCode

    open fun getErrorMessageArguments(): Array<Any?>? = null

    fun getErrorMessage(errorMessageSource: ResourceBundleMessageSource): BaseMessage {
        val errorMessage = try {
            errorMessageSource.getMessage(errorCode().name, getErrorMessageArguments(), LocaleContextHolder.getLocale())
        } catch (e: Exception) {
            e.message
        }
        return BaseMessage(errorCode().code, errorMessage)
    }
}

class QuestionNotFoundException(private val id: Long) : TestServiceException() {
    override fun errorCode() = ErrorCode.QUESTION_NOT_FOUND
    override fun getErrorMessageArguments(): Array<Any?> = arrayOf(id)
}
class AnswerIsNotEmpty : TestServiceException() {
    override fun errorCode() = ErrorCode.ANSWER_IS_NOT_EMPTY
}