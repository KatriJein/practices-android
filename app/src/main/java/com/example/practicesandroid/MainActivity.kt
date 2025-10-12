package com.example.practicesandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.practicesandroid.ui.theme.PracticesAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PracticesAndroidTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun ContentBlue(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
    )
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    PracticesAndroidTheme {
//        Greeting("Android")
//    }
//}