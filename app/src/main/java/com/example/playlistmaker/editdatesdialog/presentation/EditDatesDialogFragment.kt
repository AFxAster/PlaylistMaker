package com.example.playlistmaker.editdatesdialog.presentation

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.EditDatesDialogBinding
import com.example.playlistmaker.released.presentation.ReleasedViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditDatesDialogFragment : DialogFragment() {

    private var _binding: EditDatesDialogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReleasedViewModel by activityViewModel()

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private val startDateSetListener =
        OnDateSetListener { datePicker, year, month, day ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day, 0, 0, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            selectedStartDate = calendar
            binding.startEditText.setText(dateFormat.format(calendar.timeInMillis))
        }

    private val endDateSetListener =
        OnDateSetListener { datePicker, year, month, day ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, day, 0, 0, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            selectedEndDate = calendar
            binding.endEditText.setText(dateFormat.format(calendar.timeInMillis))
        }

    private var selectedStartDate: Calendar? = null
    private var selectedEndDate: Calendar? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = EditDatesDialogBinding.inflate(layoutInflater)
        val dialogBuilder = MaterialAlertDialogBuilder(requireContext(), R.style.ConfirmationDialog)
            .setView(binding.root)
            .setTitle(R.string.edit_dates)
            .setPositiveButton(R.string.ok, null)
            .setNegativeButton(R.string.cancel, null)

        initView()

        return dialogBuilder.create()
    }

    override fun onResume() {
        (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE).setOnClickListener {
            onPositiveClick()
        }
        super.onResume()
    }

    private fun onPositiveClick() {
        if (selectedStartDate != null && selectedEndDate != null) {
            if (selectedStartDate!! <= selectedEndDate!!) {
                saveDates()
                dismiss()
            } else
                binding.invalidWarning.isVisible = selectedStartDate!! > selectedEndDate!!
        } else {
            saveDates()
            dismiss()
        }
    }

    private fun saveDates() {
        viewModel.startDate = selectedStartDate
        viewModel.endDate = selectedEndDate

        viewModel.loadData()
    }

    private fun initView() {
        viewModel.startDate?.let {
            selectedStartDate = it
            binding.startEditText.setText(dateFormat.format(it.timeInMillis))
        }
        binding.startEditText.setOnClickListener {
            val savedDate = selectedStartDate ?: Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(requireContext(), R.style.DatePicker)
            datePickerDialog.updateDate(
                savedDate.get(Calendar.YEAR),
                savedDate.get(Calendar.MONTH),
                savedDate.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.setOnDateSetListener(startDateSetListener)
            datePickerDialog.show()
        }

        viewModel.endDate?.let {
            selectedEndDate = it
            binding.endEditText.setText(dateFormat.format(it.timeInMillis))
        }
        binding.endEditText.setOnClickListener {
            val savedDate = selectedEndDate ?: Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(requireContext(), R.style.DatePicker)
            datePickerDialog.updateDate(
                savedDate.get(Calendar.YEAR),
                savedDate.get(Calendar.MONTH),
                savedDate.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.setOnDateSetListener(endDateSetListener)
            datePickerDialog.show()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}