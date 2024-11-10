package com.example.playlistmaker.newplaylist.presentation

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.example.playlistmaker.newplaylist.presentation.viewmodel.NewPlaylistViewModel
import com.example.playlistmaker.playlist.domain.entity.Playlist
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.markodevcic.peko.PermissionRequester
import com.markodevcic.peko.PermissionResult
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {
    private lateinit var binding: FragmentNewPlaylistBinding
    private val requester = PermissionRequester.instance()
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            setUri(uri)
        }
    private val exitDialog by lazy {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.ask_finish_creating_playlist)
            .setMessage(R.string.lost_data_warning)
            .setNegativeButton(R.string.cancel) { _, _ -> }
            .setPositiveButton(R.string.quit) { _, _ ->
                back()
            }.create()
    }
    private var lastUri: Uri? = null

    private val viewModel: NewPlaylistViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentNewPlaylistBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            toolbar.setNavigationOnClickListener {
                onBack()
            }
            nameEditText.doOnTextChanged { text, _, _, _ ->
                createButton.isEnabled = text?.isNotBlank() == true
            }
            artwork.setOnClickListener {
                lifecycleScope.launch {
                    requester.request(Manifest.permission.READ_EXTERNAL_STORAGE).collect { result ->
                        when (result) {
                            is PermissionResult.Granted -> {
                                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            }

                            else -> {}
                        }
                    }
                }
            }
            createButton.setOnClickListener {
                val playlist = Playlist(
                    0,
                    nameEditText.text.toString().trim(),
                    descriptionEditText.text.toString(),
                    lastUri?.toString(),
                    emptyList()
                )
                viewModel.addPlaylist(playlist)

                showToast()
                back()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            onBack()
        }
    }

    private fun showToast() {
        Toast.makeText(
            requireContext(),
            "${getString(R.string.playlist)} ${binding.nameEditText.text.toString()} ${getString(R.string.was_created).lowercase()}",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun onBack() {
        if (lastUri != null ||
            binding.nameEditText.text?.isNotBlank() == true ||
            binding.descriptionEditText.text?.isNotBlank() == true
        ) {
            exitDialog.show()
        } else {
            back()
        }
    }

    private fun back() {
        try {
            findNavController().navigateUp()
        } catch (e: IllegalStateException) {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setUri(uri: Uri?) {
        if (uri != null) {
            binding.artwork.setImageURI(uri)
            lastUri = uri
        }
    }
}