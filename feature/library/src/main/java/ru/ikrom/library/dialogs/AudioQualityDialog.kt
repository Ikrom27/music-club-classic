package ru.ikrom.library.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AudioQualityDialog : DialogFragment() {
    interface QualitySelectionListener {
        fun onQualitySelected(quality: AudioQuality)
    }

    enum class AudioQuality { AUTO, LOW, HIGH }

    private var listener: QualitySelectionListener? = null
    private var currentQuality: AudioQuality = AudioQuality.AUTO

    fun setParams(currentQuality: AudioQuality, listener: QualitySelectionListener) {
        this.currentQuality = currentQuality
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val qualities = arrayOf("Авто (рекомендуется)", "Низкое", "Высокое")
        val initialSelection = currentQuality.ordinal

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Качество аудио")
            .setSingleChoiceItems(qualities, initialSelection) { dialog, which ->
                val selectedQuality = AudioQuality.values()[which]
                listener?.onQualitySelected(selectedQuality)
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}