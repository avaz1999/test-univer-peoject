package com.example.testuniverproject.app

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserService {
    fun create(user: UserDto)
    fun edit(id: Long, userDto: UserDto)
    fun getOne(id: Long): UserDto
    fun getAll(pageable: Pageable): Page<UserDto>
    fun delete(id: Long)
}

interface QuestionService {
    fun createQuestion(question: QuestionDto)
    fun edit(id: Long, title: String)
    fun getOne(id: Long): QuestionDto
    fun getAll(pageable: Pageable): Page<QuestionDto>
    fun delete(id: Long)
}

interface AnswerService {
    fun create(questionId: Long, answer: AnswerDto)
    fun edit(id: Long, answer: AnswerDto)
    fun delete(id: Long)

}


@Service
class UserServiceImpl(
    private val repository: UserRepository
) : UserService {
    override fun create(user: UserDto) {
        repository.save(User(user.firstName, user.lastName, user.username, user.password, UserRole.CLIENT))
    }

    override fun edit(id: Long, userDto: UserDto) {
        val user = repository.findByIdAndDeletedFalse(id)
        user.let {
            it.firstName = userDto.firstName
            it.lastName = userDto.lastName
            it.username = userDto.username
            it.password = userDto.password
        }
        repository.save(user)
    }

    override fun getOne(id: Long): UserDto {
        val user = repository.findByIdAndDeletedFalse(id)
        return UserDto.toDto(user)
    }

    override fun getAll(pageable: Pageable) = repository.findAllByDeletedFalse(pageable).map { UserDto.toDto(it) }

    override fun delete(id: Long) {
        val user = repository.findByIdAndDeletedFalse(id)
        user.deleted = true
        repository.save(user)
    }
}

@Service
class QuestionServiceImpl(
    private val repository: QuestionRepository,
    private val answerRepository: AnswerRepository
) : QuestionService {
    @Transactional
    override fun createQuestion(question: QuestionDto) {
        if (question.answer!!.isEmpty()) throw AnswerIsNotEmpty()
        val saveQuestion = repository.save(Question(question.title))
        val answerList: MutableList<Answer> = mutableListOf()
        question.answer.forEach { item ->
            answerList.add(Answer(item.variant, saveQuestion, item.trueOrFalse))
        }
        Question(question.title, answerList)
        repository.save(repository.findByIdAndDeletedFalse(saveQuestion.id!!)!!)
    }

    override fun edit(id: Long, title: String) {
        val question = repository.findByIdAndDeletedFalse(id) ?: throw QuestionNotFoundException(id)
        question.title = title
        repository.save(question)
    }

    override fun getOne(id: Long): QuestionDto {
        val question = repository.findByIdAndDeletedFalse(id) ?: throw QuestionNotFoundException(id)
        val answerDtoList: MutableList<AnswerDto> = mutableListOf()
        question.answers.forEach { item ->
            answerDtoList.add(AnswerDto(item.variant))
        }
        return QuestionDto.toDto(question, answerDtoList)
    }

    override fun getAll(pageable: Pageable) =
        repository.findAllByDeletedFalse(pageable).map { QuestionDto.toDtoList(it) }

    override fun delete(id: Long) {
        val question = repository.findByIdAndDeletedFalse(id) ?: throw QuestionNotFoundException(id)
        question.deleted = true
        repository.save(question)
    }
}

@Service
class AnswerServiceImpl(
    private val repository: AnswerRepository,
    private val questionRepository: QuestionRepository
) : AnswerService {
    override fun create(questionId: Long, answer: AnswerDto) {
        val question = questionRepository.findByIdAndDeletedFalse(questionId)
            ?: throw QuestionNotFoundException(questionId)
        repository.save(Answer(answer.variant, question, answer.trueOrFalse))
    }

    override fun edit(id: Long, answer: AnswerDto) {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }
}