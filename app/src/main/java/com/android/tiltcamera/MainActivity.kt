package com.android.tiltcamera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.android.tiltcamera.ui.theme.TiltCameraTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT,
            ),
            navigationBarStyle = SystemBarStyle.dark(
                android.graphics.Color.TRANSPARENT,
            )
        )
        setContent {
            TiltCameraTheme {
                val viewModel = viewModel<MainViewModel>()


                    Column(modifier = Modifier.fillMaxSize()
                        .background(brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFD5185C),Color(0xFF31338A )),
                            start = Offset(0f, Float.POSITIVE_INFINITY),
                            end = Offset(Float.POSITIVE_INFINITY, 0f)
                        )),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Column(modifier = Modifier
                            .size(160.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally){
                            Text(text = String.format(Locale.getDefault(), "Azimuth=%.2f°", viewModel.azimuth))
                            Text(text = String.format(Locale.getDefault(), "Pitch=%.2f°", viewModel.pitch))
                            Text(text = String.format(Locale.getDefault(), "Roll=%.2f°", viewModel.roll))

                        }



                }
            }
        }
    }
}

