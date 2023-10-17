package com.example.testuniverproject.app

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
class BaseEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,
    @LastModifiedDate @Temporal(TemporalType.TIMESTAMP) var modifiedDate: Date? = null,
    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false,
)

@Entity
class User(
    var firstName: String,
    var lastName: String,
    var username: String,
    var password: String,
    @Enumerated(EnumType.STRING) var role: UserRole
) : BaseEntity()

@Entity
class Sessions(
    var time: TimerTask,
    var result: Double,
    var numberOfTests: Short,
    var correctAnswer: Short,
    @ManyToOne var user: User,
    @OneToMany var questions: List<Question>
) : BaseEntity()

@Entity
class Question(
    var title: String,
    @OneToMany(mappedBy = "question") var answers: List<Answer> = mutableListOf()
) : BaseEntity()

@Entity
class Answer(
    var variant: String,
    @ManyToOne var question: Question? = null,
    var tureOrFalse: Boolean = false
) : BaseEntity()