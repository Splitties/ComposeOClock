package com.louiscad.composeoclockplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.louiscad.composeoclockplayground.ui.theme.MyComposeOClockPlaygroundTheme
import org.splitties.compose.oclock.OClockRootCanvas
import org.splitties.compose.oclock.sample.watchfaces.WatchFaceSwitcher

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyComposeOClockPlaygroundTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Content(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        OClockRootCanvas {
            WatchFaceSwitcher()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContentPreview() {
    MyComposeOClockPlaygroundTheme {
        Content()
    }
}
