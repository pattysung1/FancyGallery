package edu.vt.cs5254.fancygallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import coil.Coil
import coil.imageLoader
import edu.vt.cs5254.fancygallery.databinding.FragmentGalleryBinding
import kotlinx.coroutines.launch

class GalleryFragment : Fragment(){

    private var _binding: FragmentGalleryBinding? = null
    private val binding
        get() = checkNotNull(_binding){"FGalleryBinding is null"}

    private val vm: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container,false)
        binding.photoGrid.layoutManager = GridLayoutManager(context, 3)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.galleryItems.collect {items ->
                    binding.photoGrid.adapter = GalleryItemAdapter(items) {photoPageUri ->
                        findNavController().navigate(
                            GalleryFragmentDirections.showPhoto(photoPageUri)
                        )
                    }
                }
            }
        }

        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.reload_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.reload_menu -> {
                        viewLifecycleOwner.lifecycleScope.launch {
                            context?.imageLoader?.memoryCache?.clear()
                            vm.reloadGalleryItems()
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner)

    }
////ADD Menu Gallery
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.reload_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId) {
//            R.id.reload_menu -> {
//                // 执行清除 Coil 图像加载器缓存的操作
//                vm.reloadGalleryItems()
//                return true
//            }
//            else -> return super.onOptionsItemSelected(item)
//        }
//    }
//    private fun clearImageLoaderCache() {
//        val imageLoader = Coil.imageLoader(requireContext())
//        imageLoader.memoryCache?.clear()
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setHasOptionsMenu(true) // 允许Fragment显示菜单
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}