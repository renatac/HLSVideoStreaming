package com.example.hlsvideostreaming

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.hlsvideostreaming.presentation.view.components.PlayerScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayerScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PlayerScreen()
}