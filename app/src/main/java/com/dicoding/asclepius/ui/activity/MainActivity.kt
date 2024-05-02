package com.dicoding.asclepius.ui.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.asclepius.R
import com.dicoding.asclepius.data.model.UserActivity
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.ui.viewmodel.MainViewModel
import com.dicoding.asclepius.utils.helper.ImageClassifierHelper
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.io.File
import java.text.NumberFormat
import java.util.UUID

class MainActivity : AppCompatActivity() {
    private lateinit var layout: ActivityMainBinding
    private var imageUri: Uri? = null
    private var imageClassifier: ImageClassifierHelper? = null
    private val viewModel: MainViewModel by viewModels<MainViewModel> {
        MainViewModel.Factory(
            application
        )
    }
    private val imageProcessor = ImageProcessor(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        layout = ActivityMainBinding.inflate(layoutInflater)
        setContentView(layout.root)

        setupUI()
    }

    private fun setupUI() {
        setupToolbar()
        setupButtons()
    }

    private fun setupToolbar() {
        layout.mainToolbar.apply {
            inflateMenu(R.menu.main_menu)
            setOnMenuItemClickListener {
                if (it.itemId == R.id.menu_history) {
                    navigateToUserHistory()
                }
                true
            }
        }
    }

    private fun setupButtons() {
        layout.galleryButton.setOnClickListener { imageProcessor.startGallery() }
        layout.analyzeButton.setOnClickListener { imageProcessor.analyzeImage() }
    }

    private fun navigateToUserHistory() {
        startActivity(Intent(this, UserHistoryActivity::class.java))
    }

    private inner class ImageProcessor(private val activity: MainActivity) {
        private val cropImageContract = createCropImageContract()
        private val cropImageLauncher = registerForActivityResult(cropImageContract) { uri ->
            setImageUri(uri)
        }
        private val galleryLauncher = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri: Uri? ->
            if (uri != null) {
                setImageUri(uri)
                showImage()
            } else {
                Log.d("Photo Picker", "No media selected")
            }
        }

        private fun createCropImageContract() = object : ActivityResultContract<Uri, Uri>() {
            override fun createIntent(context: Context, input: Uri) = UCrop.of(
                input, Uri.fromFile(
                    File(
                        cacheDir,
                        StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()
                    )
                )
            ).withOptions(UCrop.Options()).getIntent(context)

            override fun parseResult(resultCode: Int, intent: Intent?) = UCrop.getOutput(intent!!)!!
        }

        fun startGallery() {
            galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        fun showImage() {
            imageUri?.let { cropImageLauncher.launch(it) }
        }

        fun analyzeImage() {
            if (imageClassifier == null) {
                setupImageClassifier()
            }
            imageUri?.let { imageClassifier?.classifyStaticImage(it) }
        }

        private fun setupImageClassifier() {
            imageClassifier = ImageClassifierHelper(
                appContext = activity,
                classificationListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onError(error: String) {
                        lifecycleScope.launch(Dispatchers.Main) { showToast(error) }
                    }

                    override fun onResults(results: List<Classifications>?) {
                        lifecycleScope.launch(Dispatchers.Main) {
                            handleClassificationResults(results)
                        }
                    }
                }
            )
        }

        private fun handleClassificationResults(results: List<Classifications>?) {
            results?.takeIf { it.isNotEmpty() && it[0].categories.isNotEmpty() }
                ?.let { categoriesList ->
                    val displayResult =
                        "${categoriesList[0].categories[0].label} " + NumberFormat.getPercentInstance()
                            .format(categoriesList[0].categories[0].score)
                            .trim()
                    saveToHistory(displayResult)
                    moveToResult(displayResult)
                } ?: showToast("There is no result!")
        }
    }

    private fun setImageUri(uri: Uri) {
        layout.apply {
            imageUri = uri
            previewImageView.setImageURI(imageUri)
        }
    }

    private fun saveToHistory(result: String) {
        viewModel.insertHistory(
            UserActivity(
                activityImageUri = imageUri.toString(),
                activityResults = result
            )
        )
    }

    private fun moveToResult(result: String) {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra(ResultActivity.EXTRA_RESULT, result)
        intent.putExtra(ResultActivity.EXTRA_IMAGE, imageUri.toString())
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}