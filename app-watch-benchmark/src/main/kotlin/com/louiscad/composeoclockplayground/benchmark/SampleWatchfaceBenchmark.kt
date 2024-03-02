package com.louiscad.composeoclockplayground.benchmark

import android.content.ComponentName

class SampleWatchfaceBenchmark : WatchFaceBenchmark() {
    override val watchfaceComponent: ComponentName = ComponentName(
        PACKAGE_NAME,
        "com.louiscad.composeoclockplayground.SampleWatchFaceService"
    )
}