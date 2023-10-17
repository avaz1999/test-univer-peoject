package com.example.testuniverproject.app

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/test")
class UserController(
    private val service: UserService
) {
    @PostMapping
    fun createUser(@RequestBody user: UserDto) = service.create(user)

    @PutMapping("{id}")
    fun edit(@PathVariable id: Long, @RequestBody user: UserDto) = service.edit(id, user)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)

    @GetMapping
    fun getAll(pageable: Pageable) = service.getAll(pageable)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}

@RestController
@RequestMapping("api/question")
class QuestionController(
    private val service: QuestionService
) {
    @PostMapping
    fun create(@RequestBody question: QuestionDto) = service.createQuestion(question)

    @PutMapping("{id}")
    fun edit(@PathVariable id: Long, title: String) = service.edit(id, title)

    @GetMapping("{id}")
    fun getOne(@PathVariable id: Long) = service.getOne(id)

    @GetMapping
    fun getAll(pageable: Pageable) = service.getAll(pageable)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}

@RestController
@RequestMapping("api/question")
class AnswerController(
    private val service: AnswerService
) {
    @PostMapping("{questionId}")
    fun create(@PathVariable questionId: Long, @RequestBody question: AnswerDto) = service.create(questionId,question)

    @PutMapping("{id}")
    fun edit(@PathVariable id: Long, answer: AnswerDto) = service.edit(id, answer)

    @DeleteMapping("{id}")
    fun delete(@PathVariable id: Long) = service.delete(id)
}


@RestController
@RequestMapping("api/question")
class SessionController(
    private val service: QuestionService
) {
    @PostMapping
    fun createSession(@RequestBody question: SessionDto) {

    }
}