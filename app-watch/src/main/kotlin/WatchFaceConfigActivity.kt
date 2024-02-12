package com.louiscad.composeoclockplayground

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.wear.watchface.editor.EditorSession
import com.louiscad.composeoclockplayground.editor.WatchFaceConfigContent
import com.louiscad.composeoclockplayground.editor.WatchFaceEditorSession
import kotlinx.coroutines.*
import splitties.toast.longToast
import splitties.toast.toast

class WatchFaceConfigActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(start = CoroutineStart.UNDISPATCHED) {
            val activity = this@WatchFaceConfigActivity
            val editorSession = WatchFaceEditorSession(
                scope = lifecycleScope,
                session = EditorSession.createOnWatchEditorSession(activity)
            )
            setContent {
                WatchFaceConfigContent(
                    editorSession = editorSession,
                )
            }
            if (Settings.System.canWrite(applicationContext)) {
                toast("Can write settings!")
            } else {
                longToast( "Go into \"Advcanced\"…")
                longToast( "and enable \"Modify system settings\"")
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", packageName, null)
                    )
                )
            }
        }
    }
}
