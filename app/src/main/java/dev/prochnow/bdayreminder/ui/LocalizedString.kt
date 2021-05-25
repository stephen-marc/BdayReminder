package dev.prochnow.bdayreminder.ui

import android.content.Context
import android.widget.TextView
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

sealed class LocalizedString {

    object EmptyString : LocalizedString()

    data class RawString(val stringValue: String) : LocalizedString()

    data class ResourceString(
        @StringRes val resourceId: Int,
        val args: List<Any>
    ) : LocalizedString()

    data class QuantityResourceString(
        @PluralsRes val resourceId: Int,
        val quantity: Int,
        val args: List<Any>
    ) : LocalizedString()

    data class CompoundString(val parts: List<LocalizedString>) : LocalizedString() {

        override operator fun plus(other: LocalizedString) = CompoundString(this.parts + other)

        override operator fun plus(other: CompoundString) = CompoundString(this.parts + other.parts)
    }

    open operator fun plus(other: LocalizedString) = CompoundString(listOf(this, other))

    open operator fun plus(other: CompoundString) = CompoundString(listOf(this) + other.parts)

    companion object {

        fun empty() = EmptyString

        fun raw(value: String): LocalizedString = when (value) {
            "" -> EmptyString
            else -> RawString(value)
        }

        fun resource(@StringRes resourceId: Int, vararg args: Any) =
            ResourceString(resourceId, args.toList())

        fun quantity(@PluralsRes resourceId: Int, quantity: Int, vararg args: Any) =
            QuantityResourceString(resourceId, quantity, args.toList())
    }
}

fun LocalizedString.get(context: Context): String = when (this) {
    LocalizedString.EmptyString -> ""
    is LocalizedString.RawString -> stringValue
    is LocalizedString.ResourceString -> {
        context.getString(resourceId, *getArgValues(context, args))
    }
    is LocalizedString.QuantityResourceString -> {
        context.resources.getQuantityString(resourceId, quantity, *getArgValues(context, args))
    }
    is LocalizedString.CompoundString -> {
        parts.joinToString(separator = "") { it.get(context) }
    }
}

var TextView.localizedText: LocalizedString
    get() = LocalizedString.raw(text.toString())
    set(value) {
        text = value.get(this.context)
    }

private fun getArgValues(context: Context, args: List<Any>): Array<Any> {
    return args.map {
        if (it is LocalizedString) {
            it.get(context)
        } else {
            it
        }
    }.toTypedArray()
}
