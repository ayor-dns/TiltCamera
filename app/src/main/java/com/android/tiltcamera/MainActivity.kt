package com.android.tiltcamera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.android.tiltcamera.app.Route
import com.android.tiltcamera.camera.presentation.CameraScreenRoot
import com.android.tiltcamera.camera.presentation.CameraViewModel
import com.android.tiltcamera.core.presentation.TiltCameraTheme
import com.android.tiltcamera.gallery.presentation.collection_detail.GalleryDetailScreenRoot
import com.android.tiltcamera.gallery.presentation.collection_detail.GalleryDetailViewModel
import com.android.tiltcamera.gallery.presentation.collection_gallery.GalleryScreenRoot
import com.android.tiltcamera.gallery.presentation.collection_gallery.GalleryViewModel
import com.android.tiltcamera.gallery.presentation.picture_detail.PictureDetailScreenRoot
import com.android.tiltcamera.gallery.presentation.picture_detail.PictureDetailViewModel
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
                                viewModel = viewModel,
                                onCollectionClick = { collection ->
                                    navController.navigate(Route.GalleryDetailScreen(collection.collectionId))
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }

                            )
                        }

                        composable<Route.GalleryDetailScreen>(
                            exitTransition = { slideOutHorizontally() },
                            popEnterTransition = { slideInHorizontally() }
                        ) {entry ->

                            val viewModel = hiltViewModel<GalleryDetailViewModel>()
                            viewModel.initialize(entry.toRoute<Route.GalleryDetailScreen>().collectionId)

                            GalleryDetailScreenRoot(
                                viewModel = viewModel,
                                onPictureClick = { picture ->
                                    navController.navigate(Route.PictureDetailScreen(picture.pictureId))
                                },
                                onBackClick = {
                                    navController.popBackStack()
                                }
                            )

                        }

                        composable<Route.PictureDetailScreen>(
                            exitTransition = { slideOutHorizontally() },
                            popEnterTransition = { slideInHorizontally() }
                        ) {entry ->

                            val viewModel = hiltViewModel<PictureDetailViewModel>()
                            viewModel.initialize(entry.toRoute<Route.PictureDetailScreen>().pictureId)

                            PictureDetailScreenRoot(
                                viewModel = viewModel,
                                onBackClick = {
                                    navController.popBackStack()
                                }
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

