package com.example.hlsvideostreaming.presentation.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.hlsvideostreaming.R

@Composable
fun PlayerScreen() {
    var videoUrl by remember { mutableStateOf("") }
    var isVideoReady by remember { mutableStateOf(false) }
    var isError by remember { mutableStateOf(false) }
    var playbackError by remember { mutableStateOf<PlaybackException?>(null) }
    var showErrorMessage by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var player: ExoPlayer? by remember { mutableStateOf(null) }

    fun initializePlayer(url: String) {
        player?.release()
        player = ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.Builder()
                .setUri(url)
                .build()
            setMediaItem(mediaItem)
            prepare()
            play()
            addListener(object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    playbackError = error
                    isError = true
                    showErrorMessage = true
                    isVideoReady = false
                }
            })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (isVideoReady) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            this.player = player
                            useController = true
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                    update = { playerView ->
                        playerView.player = player
                    }
                )
            }
        }
        if (showErrorMessage) {
            val errorMessage = stringResource(id = R.string.error_message)
            Text(
                text = "$errorMessage ${playbackError?.message ?: "Erro desconhecido"}",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        val outlinedTextLabel = stringResource(id = R.string.type_video_url)
        OutlinedTextField(
            value = videoUrl,
            onValueChange = { videoUrl = it },
            label = { Text(outlinedTextLabel) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (videoUrl.isNotBlank()) {
                initializePlayer(videoUrl)
                isVideoReady = true
                showErrorMessage = false
            }
        }) {
            val buttonText = stringResource(id = R.string.play_video)
            Text(buttonText)
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            player?.release()
        }
    }
}