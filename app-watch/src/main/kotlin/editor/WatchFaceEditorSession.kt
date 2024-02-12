package com.louiscad.composeoclockplayground.editor

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.wear.watchface.complications.ComplicationDataSourceInfo
import androidx.wear.watchface.editor.EditorSession
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class WatchFaceEditorSession(
    scope: CoroutineScope,
    val session: EditorSession?
) {
    fun tryOpenDataSourcePicker(complicationId: Int) {
        flow.tryEmit(complicationId)
    }

    fun complicationDataSourceInfo(id: Int): Flow<ComplicationDataSourceInfo?> = session?.let {
        it.complicationsDataSourceInfo.map { map -> map[id] }
    } ?: flowOf(null)

    private val isBusyPickingComplicationState = mutableStateOf(false)

    val isBusyPickingDataSource: Boolean by isBusyPickingComplicationState

    private val flow = MutableSharedFlow<Int>(extraBufferCapacity = 1)

    init {
        scope.launch {
            flow.collect { id ->
                isBusyPickingComplicationState.withValue(valueInScope = true) {
                    session?.openComplicationDataSourceChooser(id)
                }
            }
        }
    }
}

private inline fun <T, R> MutableState<T>.withValue(
    valueInScope: T,
    block: () -> R
): R {
    val initialValue = value
    try {
        value = valueInScope
        return block()
    } finally {
        value = initialValue
    }
}
