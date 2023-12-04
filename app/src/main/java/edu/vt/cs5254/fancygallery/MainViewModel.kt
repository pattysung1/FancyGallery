package edu.vt.cs5254.fancygallery

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.vt.cs5254.fancygallery.api.GalleryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "PhotoGalleryViewModel"

class MainViewModel : ViewModel() {
    private val photoRepository = PhotoRepository()

    private val _galleryItems: MutableStateFlow<List<GalleryItem>> =
        MutableStateFlow(emptyList())
    val galleryItems: StateFlow<List<GalleryItem>>
        get() = _galleryItems.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val items = photoRepository.fetchPhotos(99)
                Log.d(TAG, "Items received: $items")
                _galleryItems.value = loadPhotos()
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to fetch gallery items", ex)
            }
        }
    }

    // 私有的加载照片函数，与之前示例中的 loadPhotos() 相同
    private suspend fun loadPhotos(): List<GalleryItem> {
        return try {
            val items = photoRepository.fetchPhotos(99)
            Log.d(TAG, "Items received: $items")
            items
        } catch (ex: Exception) {
            Log.e(TAG, "Failed to fetch gallery items", ex)
            emptyList()
        }
    }
    // 添加 reloadGalleryItems() 函数
    suspend fun reloadGalleryItems() {
        _galleryItems.value = emptyList()
        _galleryItems.update {
            loadPhotos()
        }
    }
}