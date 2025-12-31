package top.kmiit.debughelper.utils

import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.GLES20
import android.util.Log

data class GpuInfo(
    val renderer: String = "",
    val vendor: String = "",
    val version: String = "",
    val extensions: String = "",
    val vulkanVersion: String = ""
)

object GpuUtils {
    init {
        try {
            System.loadLibrary("dh")
        } catch (e: UnsatisfiedLinkError) {
            Log.e("GpuViewModel", "Failed to load library dh", e)
        }
    }
    external fun getVulkanVersionNative(): String

    fun getGpuInfo(): GpuInfo {
        val (renderer, vendor, version, extensions) = getGlInfo()
        val vulkanVersion = getVulkanInfo()
        return GpuInfo(renderer, vendor, version, extensions, vulkanVersion)
    }

    private fun getGlInfo(): List<String> {
        try {
            val dpy = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
            val versionArr = IntArray(2)
            EGL14.eglInitialize(dpy, versionArr, 0, versionArr, 1)

            val configAttribs = intArrayOf(
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
                EGL14.EGL_NONE
            )
            val configs = arrayOfNulls<EGLConfig>(1)
            val numConfigs = IntArray(1)
            EGL14.eglChooseConfig(dpy, configAttribs, 0, configs, 0, 1, numConfigs, 0)
            
            if (numConfigs[0] == 0) {
                 return listOf("", "", "", "")
            }
            
            val config = configs[0]

            val ctxAttribs = intArrayOf(
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
            )
            val ctx = EGL14.eglCreateContext(dpy, config, EGL14.EGL_NO_CONTEXT, ctxAttribs, 0)

            val surfAttribs = intArrayOf(
                EGL14.EGL_WIDTH, 1,
                EGL14.EGL_HEIGHT, 1,
                EGL14.EGL_NONE
            )
            val surf = EGL14.eglCreatePbufferSurface(dpy, config, surfAttribs, 0)

            EGL14.eglMakeCurrent(dpy, surf, surf, ctx)

            val renderer = GLES20.glGetString(GLES20.GL_RENDERER) ?: ""
            val vendor = GLES20.glGetString(GLES20.GL_VENDOR) ?: ""
            val version = GLES20.glGetString(GLES20.GL_VERSION) ?: ""
            val extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS) ?: ""

            EGL14.eglMakeCurrent(dpy, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
            EGL14.eglDestroySurface(dpy, surf)
            EGL14.eglDestroyContext(dpy, ctx)
            EGL14.eglTerminate(dpy)

            return listOf(renderer, vendor, version, extensions)
        } catch (e: Exception) {
            e.printStackTrace()
            return listOf("", "", "", "")
        }
    }

    private fun getVulkanInfo(): String {
        try {
            val nativeVer = getVulkanVersionNative()
            if (nativeVer.isNotEmpty() && nativeVer != "Unknown") {
                return nativeVer
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return "Not Supported"
    }
}
