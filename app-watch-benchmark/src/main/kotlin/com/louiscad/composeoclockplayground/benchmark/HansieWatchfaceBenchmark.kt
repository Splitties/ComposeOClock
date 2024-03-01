package com.louiscad.composeoclockplayground.benchmark

import android.content.ComponentName

class HansieWatchfaceBenchmark : WatchFaceBenchmark() {
    override val watchfaceComponent: ComponentName = ComponentName(
        PACKAGE_NAME,
        "com.louiscad.composeoclockplayground.samples.HansieWatchFaceService"
    )
}