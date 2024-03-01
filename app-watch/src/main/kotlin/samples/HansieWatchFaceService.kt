package com.louiscad.composeoclockplayground.samples

import androidx.compose.runtime.Composable
import androidx.wear.watchface.complications.data.ComplicationData
import androidx.wear.watchface.complications.data.ComplicationType
import kotlinx.coroutines.flow.StateFlow
import org.splitties.compose.oclock.ComposeWatchFaceService
import org.splitties.compose.oclock.sample.watchfaces.hansie.HansieClock

class HansieWatchFaceService : ComposeWatchFaceService(
    complicationSlotIds = emptySet(),
    invalidationMode = InvalidationMode.WaitForInvalidation
) {

    @Composable
    override fun Content(complicationData: Map<Int, StateFlow<ComplicationData>>) {
        HansieClock()
    }

    override fun supportedComplicationTypes(slotId: Int) = listOf(
        ComplicationType.RANGED_VALUE,
        ComplicationType.SHORT_TEXT
    )
}
