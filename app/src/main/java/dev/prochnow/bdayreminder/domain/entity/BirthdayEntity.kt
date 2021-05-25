package dev.prochnow.bdayreminder.domain.entity

import java.time.LocalDate
import java.util.*


data class BirthdayEntity(
    val uuid: UUID,
    val personName: String,
    val date: LocalDate,
    val category: CategoryEntity
)

enum class CategoryEntity {
    NONE, FAMILY, FRIENDS, WORK
}
