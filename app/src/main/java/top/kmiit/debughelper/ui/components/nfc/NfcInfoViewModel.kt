package top.kmiit.debughelper.ui.components.nfc

import android.app.Application
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class NfcTestViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>()
    private val adapter = NfcAdapter.getDefaultAdapter(context)

    val isSecureNfcSupported = adapter?.isSecureNfcSupported ?: false
    val isSecureNfcEnabled = adapter?.isSecureNfcEnabled ?: false
    val isEnabled = adapter?.isEnabled ?: false

    var tagInfo by mutableStateOf("")
        private set

    fun processTag(tag: Tag) {
        val id = tag.id.joinToString(":") { "%02X".format(it) }
        val techs = tag.techList.joinToString("\n") { it.substringAfterLast(".") }
        tagInfo = "ID: $id\nTechs:\n$techs"
    }
}
