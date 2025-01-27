package com.android.tiltcamera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.android.tiltcamera.app.Route
import com.android.tiltcamera.camera.presentation.CameraScreenRoot
import com.android.tiltcamera.camera.presentation.CameraViewModel
import com.android.tiltcamera.core.presentation.TiltCameraTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA
        )
    }

    private var hasRequiredPermissions = false

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        hasRequiredPermissions = hasRequiredPermissions()

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
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = Route.CameraGraph
                ) {

                    navigation<Route.CameraGraph>(
                        startDestination = Route.CameraScreen
                    ) {

                        composable<Route.CameraScreen>(
                            exitTransition = { slideOutHorizontally() },
                            popEnterTransition = { slideInHorizontally() }
                        ) {

                            val viewModel = hiltViewModel<CameraViewModel>()

                            CameraScreenRoot(
                                viewModel = viewModel,
                            )

                        }

                        composable<Route.GalleryScreen>(
                            exitTransition = { slideOutHorizontally() },
                            popEnterTransition = { slideInHorizontally() }
                        ) {

                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hasRequiredPermissions = hasRequiredPermissions()
    }

    override fun onPause() {
        super.onPause()
        hasRequiredPermissions = hasRequiredPermissions()
    }

}

