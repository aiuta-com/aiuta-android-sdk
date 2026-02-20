package sample.tryon.kmp.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import sample.tryon.kmp.AiutaApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup insets
        enableEdgeToEdge()

        setContent {
            AiutaApp()
        }
    }
}
