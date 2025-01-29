package com.android.tiltcamera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PowerManager
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.android.tiltcamera.gallery.presentation.GalleryScreenRoot
import com.android.tiltcamera.gallery.presentation.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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

                            LaunchedEffect(Unit) {
                                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                            }
                            DisposableEffect(Unit) {
                                onDispose {
                                    window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                                }
                            }

                            CameraScreenRoot(
                                viewModel = viewModel,
                                onNavigate = { route ->
                                    navController.navigate(route)
                                }
                            )

                        }

                        composable<Route.GalleryScreen>(
                            exitTransition = { slideOutHorizontally() },
                            popEnterTransition = { slideInHorizontally() }
                        ) {
                            val viewModel = hiltViewModel<GalleryViewModel>()

                            GalleryScreenRoot(
                                viewModel = viewModel
                            )
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

        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

