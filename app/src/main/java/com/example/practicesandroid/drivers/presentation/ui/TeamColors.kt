package com.example.practicesandroid.drivers.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.practicesandroid.R

object TeamColorsRes {
    @Composable
    fun getColor(teamId: String?): Color {
        return when (teamId) {
            "mclaren" -> colorResource(R.color.mclaren)
            "red_bull" -> colorResource(R.color.red_bull)
            "mercedes" -> colorResource(R.color.mercedes)
            "alpine" -> colorResource(R.color.alpine)
            "aston_martin" -> colorResource(R.color.aston_martin)
            "ferrari" -> colorResource(R.color.ferrari)
            "haas" -> colorResource(R.color.haas)
            "rb" -> colorResource(R.color.rb)
            "sauber" -> colorResource(R.color.sauber)
            "williams" -> colorResource(R.color.williams)
            else -> Color.Black
        }
    }
}
