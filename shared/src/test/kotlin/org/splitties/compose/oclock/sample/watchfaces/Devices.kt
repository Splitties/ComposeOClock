package org.splitties.compose.oclock.sample.watchfaces

data class WearDevice(
    val id: String,
    val modelName: String,
    val px: Int,
    val density: Float
) {
    companion object {
        val MobvoiTicWatchPro5: WearDevice = WearDevice(
            id = "ticwatch_pro_5",
            modelName = "Mobvoi TicWatch Pro 5",
            px = 466,
            density = 2.0f,
        )

        val SamsungGalaxyWatch5: WearDevice = WearDevice(
            id = "galaxy_watch_5",
            modelName = "Samsung Galaxy Watch 5",
            px = 396,
            density = 2.0f,
        )

        val GooglePixelWatch: WearDevice = WearDevice(
            id = "pixelwatch",
            modelName = "Google Pixel Watch",
            px = 384,
            density = 2.0f,
        )
    }
}