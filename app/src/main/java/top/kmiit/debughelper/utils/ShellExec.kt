package top.kmiit.debughelper.utils

object ShellExec {
    fun run(cmd: String): String {
        val process = Runtime.getRuntime().exec(cmd)
        process.waitFor()
        val output = process.inputStream.bufferedReader().readText()
        val error = process.errorStream.bufferedReader().readText()

        return output.ifEmpty { error }
    }
}
