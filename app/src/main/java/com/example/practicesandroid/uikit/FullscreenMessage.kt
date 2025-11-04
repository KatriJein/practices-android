package com.example.practicesandroid.uikit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun FullscreenMessage(msg: String) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(Dimens.Medium), contentAlignment = Alignment.Center
    ) {
        Text(text = msg)
    }
}