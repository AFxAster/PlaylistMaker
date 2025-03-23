package com.example.playlistmaker.released.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.common.presentation.GridSpacingItemDecoration
import com.example.playlistmaker.databinding.FragmentReleasedBinding
import com.example.playlistmaker.released.domain.entity.Release
import java.util.Date

class ReleasedFragment : Fragment() {
    private var _binding: FragmentReleasedBinding? = null
    private val binding get() = _binding!!

    private val adapter = ReleaseAdapter()
    private val itemDecoration = GridSpacingItemDecoration(
        spanCount = 2,
        horizontalSpacing = 8,
        verticalSpacing = 16,
        horizontalEdgeSpacing = 16,
        verticalEdgeSpacing = 0
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReleasedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter.releases = listOf(
            Release("1", "Сложная", "Лилая", "сингл", "", Date()),
            Release("2", "Дико, например", "Pharaoh", "сингл", "", Date()),
            Release("3", "Прометей роняет факел", "Horus", "альбом", "", Date()),
            Release("4", "Не хватит сил", "слёзы в ампулах", "сингл", "", Date()),
            Release("5", "Океаны", "GUMA, TEMNEE", "сингл", "", Date()),
            Release("5", "Океаны", "GUMA, TEMNEE", "сингл", "", Date()),
            Release("5", "Океаны", "GUMA, TEMNEE", "сингл", "", Date()),
        )
        with(binding) {
            releasedRecyclerView.adapter = adapter
            releasedRecyclerView.addItemDecoration(itemDecoration)
        }
        // TODO ченкуть про соотношение в разметке для элемента
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}