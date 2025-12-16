package top.kmiit.debughelper.utils

import android.annotation.SuppressLint
import android.util.Log
import java.lang.reflect.Method

@SuppressLint("PrivateApi")
object PropFactory {
    private val getMethod: Method? by lazy {
        try {
            val clazz = Class.forName("android.os.SystemProperties")
            clazz.getMethod("get", String::class.java, String::class.java)
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Failed to initialize PropFactory", e)
            null
        }
    }

    fun get(prop: String, def: String): String {
        Log.e(this.javaClass.name, "get: $prop")
        return try {
            getMethod?.invoke(null, prop, def) as? String ?: def
        } catch (e: Exception) {
            Log.e(this.javaClass.name, "Failed to get system property", e)
            def
        }
    }
}
