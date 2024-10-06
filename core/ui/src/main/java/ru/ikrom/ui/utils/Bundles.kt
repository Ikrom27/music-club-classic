package ru.ikrom.ui.utils

import android.os.Bundle
import androidx.core.os.bundleOf

const val ID_KEY = "ID"

fun idToBundle(id: String) = bundleOf(ID_KEY to id)
fun bundleToId(bundle: Bundle): String = bundle.getString(ID_KEY) ?: ""