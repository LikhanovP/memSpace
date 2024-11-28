@file:OptIn(ExperimentalPermissionsApi::class)

package xm.space.ultimatememspace.presentation.screens.qrscanner

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.navigation.NavController
import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.Manifest
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.graphics.drawable.toBitmap
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.androidx.compose.koinViewModel
import xm.space.ultimatememspace.R
import xm.space.ultimatememspace.core.extensions.empty
import xm.space.ultimatememspace.core.uikit.components.button.OutlineMainButton
import xm.space.ultimatememspace.core.uikit.components.text.StyleBodyHint
import xm.space.ultimatememspace.core.uikit.components.text.StyleBodyLarge
import xm.space.ultimatememspace.core.uikit.components.text.StyleSubtitle
import xm.space.ultimatememspace.core.uikit.fullHorizontalLinks
import xm.space.ultimatememspace.core.uikit.fullSizeLinks
import xm.space.ultimatememspace.core.uikit.fullTopLinks
import xm.space.ultimatememspace.core.uikit.topLeftLinks
import xm.space.ultimatememspace.ui.theme.Black
import xm.space.ultimatememspace.ui.theme.Error
import xm.space.ultimatememspace.ui.theme.White

@SuppressLint("PermissionLaunchedDuringComposition")
@Composable
fun QrScannerContent(
    navController: NavController,
    vm: QrScannerViewModel = koinViewModel()
) {
    vm.navController = navController
    val barState = vm.barState.collectAsState()
    val state = vm.state.collectAsState()

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)

    BackHandler {
        vm.onCloseClick()
    }

    if (!cameraPermissionState.hasPermission) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (proceedRef, content) = createRefs()

            ConstraintLayout(modifier = Modifier.constrainAs(content) {
                fullSizeLinks()
            }) {
                val (icon, main, description) = createRefs()
                Icon(
                    modifier = Modifier.constrainAs(icon) {
                        fullHorizontalLinks()
                    },
                    painter = painterResource(id = R.drawable.ic_cross),
                    contentDescription = "",
                    tint = Error
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .constrainAs(main) {
                            fullHorizontalLinks()
                            top.linkTo(icon.bottom)
                        },
                    text = state.value.permissionTitle,
                    style = StyleSubtitle,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp, top = 8.dp)
                        .constrainAs(description) {
                            fullHorizontalLinks()
                            top.linkTo(main.bottom)
                        },
                    text = state.value.permissionDescription,
                    style = StyleBodyHint,
                    textAlign = TextAlign.Center
                )
            }

            OutlineMainButton(
                modifier = Modifier
                    .constrainAs(proceedRef) {
                        fullHorizontalLinks()
                        bottom.linkTo(parent.bottom)
                    }
                    .padding(bottom = 27.dp),
                actionTitle = state.value.proceedTitle,
                callback = {
                    cameraPermissionState.launchPermissionRequest()
                }
            )
        }
    } else {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (title, camera, close, mask) = createRefs()

            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .constrainAs(camera) {
                        fullSizeLinks()
                        height = Dimension.fillToConstraints
                    },
                factory = { androidViewContext ->
                    PreviewView(androidViewContext).apply {
                        this.scaleType = PreviewView.ScaleType.FILL_CENTER
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                        implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    }
                },
                update = { previewView ->
                    val cameraSelector: CameraSelector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                    val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
                    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                        ProcessCameraProvider.getInstance(context)

                    cameraProviderFuture.addListener({
                        preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
                        val barcodeAnalyser = BarCodeAnalyser { barcodes ->
                            barcodes.forEach { barcode ->
                                barcode.rawValue?.let { barcodeValue ->
                                    vm.onBarcodeAnalyzed(
                                        destination = barcodeValue,
                                        navController = navController
                                    )
                                }
                            }
                        }
                        val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                            }

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            Log.d("TAG", "CameraPreview: ${e.localizedMessage}")
                        }
                    }, ContextCompat.getMainExecutor(context))
                }
            )

            Text(
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { vm.onCloseClick() }
                    .constrainAs(title) { fullTopLinks() },
                text = barState.value.title,
                style = StyleBodyLarge
            )
            Image(
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(close) { topLeftLinks() }
                    .clickable { vm.onCloseClick() },
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = String.empty()
            )

            Canvas(
                modifier = Modifier
                    .size(300.dp)
                    .constrainAs(mask) {
                        fullSizeLinks()
                    },
                onDraw = {
                    ContextCompat.getDrawable(context, R.drawable.ic_camera_zoom)?.let { value ->
                        val frame = value.toBitmap()
                        val matrix = Matrix()
                        drawImage(
                            image = frame.asImageBitmap(),
                            topLeft = Offset(x = size.width * 0.07F, y = size.height * 0.07F)
                        )
                        matrix.postRotate(90F)
                        drawImage(
                            image = Bitmap.createBitmap(
                                frame,
                                0,
                                0,
                                frame.width,
                                frame.height,
                                matrix,
                                true
                            ).asImageBitmap(),
                            topLeft = Offset(x = size.width * 0.78F, y = size.height * 0.07F)
                        )
                        matrix.postRotate(180F)
                        drawImage(
                            image = Bitmap.createBitmap(
                                frame,
                                0,
                                0,
                                frame.width,
                                frame.height,
                                matrix,
                                true
                            ).asImageBitmap(),
                            topLeft = Offset(x = size.width * 0.07F, y = size.height * 0.78F)
                        )
                        matrix.postRotate(270F)
                        drawImage(
                            image = Bitmap.createBitmap(
                                frame,
                                0,
                                0,
                                frame.width,
                                frame.height,
                                matrix,
                                true
                            ).asImageBitmap(),
                            topLeft = Offset(x = size.width * 0.78F, y = size.height * 0.78F)
                        )
                    }
                }
            )
        }
    }
}
