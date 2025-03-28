package com.example.playlistmaker.released.presentation

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatAutoCompleteTextView

class AutoCompleteTextViewWithoutFiltering @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatAutoCompleteTextView(context, attrs, R.attr.autoCompleteTextViewStyle) {

    override fun performFiltering(text: CharSequence?, keyCode: Int) {
    }
}