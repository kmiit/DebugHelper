package top.kmiit.debughelper.utils

import android.content.Context
import top.kmiit.debughelper.R
import java.io.File

object NodeFactory {
    fun readSysNode(context: Context, path: String): String {
        return try {
            File(path).readText().trim()
        } catch (e: Exception) {
            context.getString(R.string.unknown)
        }
    }
}