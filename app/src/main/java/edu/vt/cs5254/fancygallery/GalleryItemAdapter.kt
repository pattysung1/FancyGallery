package edu.vt.cs5254.fancygallery

import android.net.Uri
import android.nfc.Tag
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import edu.vt.cs5254.fancygallery.api.GalleryItem
import edu.vt.cs5254.fancygallery.databinding.ListItemGalleryBinding


class GalleryItemHolder(
    private val binding: ListItemGalleryBinding
) : RecyclerView.ViewHolder(binding.root) {
    var boundGalleryItem: GalleryItem? = null
    fun bind(galleryItem: GalleryItem, onItemClicked: (Uri) -> Unit) {
        Log.d("print",galleryItem.url)
        boundGalleryItem = galleryItem
        binding.itemImageView.load(galleryItem.url){
            placeholder(R.drawable.ic_placeholder)
//            diskCachePolicy(CachePolicy.DISABLED)
        }
        binding.root.setOnClickListener{
            onItemClicked(galleryItem.photoPageUri)
        }
    }
}
class GalleryItemAdapter (
    private val galleryItems: List<GalleryItem>,
    private val onItemClicked: (Uri) -> Unit
) : RecyclerView.Adapter<GalleryItemHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GalleryItemHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGalleryBinding.inflate(inflater, parent, false)
        return GalleryItemHolder(binding)
    }

    override fun onBindViewHolder(holder: GalleryItemHolder, position: Int) {
        holder.bind(galleryItems[position], onItemClicked)
    }

    override fun getItemCount() = galleryItems.size
}