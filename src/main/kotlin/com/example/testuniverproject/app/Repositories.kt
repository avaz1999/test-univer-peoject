package com.example.testuniverproject.app

import jakarta.persistence.EntityManager
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    fun trash(id: Long): T
    fun trashList(ids: List<Long>): List<T>
    fun findAllNotDeleted(pageable: Pageable): Page<T>
    fun findAllNotDeleted(): List<T>
}

class BaseRepositoryImpl<T : BaseEntity>(
    entityInformation: JpaEntityInformation<T, Long>,
    entityManager: EntityManager,
) : SimpleJpaRepository<T, Long>(entityInformation, entityManager), BaseRepository<T> {
    val isNotDeletedSpecification = Specification<T> { root, _, cb -> cb.equal(root.get<Boolean>("deleted"), false) }

    @Transactional
    override fun trash(id: Long) = save(findById(id).get().apply { deleted = true })
    override fun findAllNotDeleted(pageable: Pageable) = findAll(isNotDeletedSpecification, pageable)
    override fun findAllNotDeleted(): List<T> = findAll(isNotDeletedSpecification)
    override fun trashList(ids: List<Long>): List<T> = ids.map { trash(it) }
}

@Repository
interface UserRepository : BaseRepository<User> {
    fun findByIdAndDeletedFalse(id: Long): User
    fun findAllByDeletedFalse(pageable: Pageable):Page<User>
}

@Repository
interface QuestionRepository : BaseRepository<Question> {
    fun findByIdAndDeletedFalse(id: Long):Question?
    fun findAllByDeletedFalse(pageable: Pageable):Page<Question>
}

@Repository
interface AnswerRepository : BaseRepository<Answer> {

}

