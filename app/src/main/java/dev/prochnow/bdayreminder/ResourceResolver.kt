package dev.prochnow.bdayreminder

import android.content.Context
import androidx.annotation.StringRes

interface ResourceResolver {
    fun getString(@StringRes res: Int): String
}


class AndroidResourceResolver(val context: Context) : ResourceResolver {
    override fun getString(res: Int): String = context.getString(res)

}
