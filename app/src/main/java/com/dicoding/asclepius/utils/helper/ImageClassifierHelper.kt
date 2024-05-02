package com.dicoding.asclepius.utils.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.io.IOException

class ImageClassifierHelper(
    private val classificationThreshold: Float = 0.1f,
    private var maximumResults: Int = 3,
    private val appContext: Context,
    private var imageClassification: ImageClassifier? = null,
    val classificationListener: ClassifierListener
) {

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        try {
            val options = buildClassifierOptions()
            imageClassification = ImageClassifier.createFromFileAndOptions(
                appContext,
                MODEL_NAME,
                options
            )
        } catch (e: IllegalStateException) {
            classificationListener.onError("Image Classification Failed")
        }
    }

    private fun buildClassifierOptions(): ImageClassifier.ImageClassifierOptions {
        return ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(classificationThreshold)
            .setMaxResults(maximumResults)
            .setBaseOptions(BaseOptions.builder().setNumThreads(4).build())
            .build()
    }

    fun classifyStaticImage(imageUri: Uri) {
        try {
            setupImageClassifier()
            val bitmap = getBitmapFromUri(imageUri)
            val results = classifyImage(bitmap)
            classificationListener.onResults(results)
        } catch (e: Exception) {
            classificationListener.onError(e.message.toString())
        }
    }

    private fun getBitmapFromUri(imageUri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(appContext.contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(appContext.contentResolver, imageUri)
            }.copy(Bitmap.Config.ARGB_8888, true)
        } catch (e: IOException) {
            null
        }
    }

    private fun classifyImage(bitmap: Bitmap?): List<Classifications>? {
        return imageClassification?.classify(
            ImageProcessor.Builder()
                .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                .add(CastOp(DataType.FLOAT32))
                .build().process(TensorImage.fromBitmap(bitmap))
        )
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?
        )
    }

    companion object {
        private const val MODEL_NAME = "cancer_classification.tflite"
    }
}