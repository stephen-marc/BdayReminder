package dev.prochnow.bdayreminder.domain.entity

import java.time.Month
import java.time.Year
import java.util.*


data class BirthdayEntity(
    val uuid: UUID,
    val personName: String,
    val birthDay: Int,
    val birthMonth: Month,
    val birthYear: Year?,
    val category: CategoryEntity
)

enum class CategoryEntity{
    NONE, FAMILY, FRIENDS, WORK
}
