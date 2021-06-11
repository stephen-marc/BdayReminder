package dev.prochnow.bdayreminder.utility

import androidx.compose.ui.text.intl.Locale
import java.time.Month
import java.time.format.TextStyle


fun Locale.toJavaLocale(): java.util.Locale =
    java.util.Locale.forLanguageTag(this.toLanguageTag())

fun Month.localizedName(locale: Locale): String =
    this.getDisplayName(TextStyle.FULL, locale.toJavaLocale())
