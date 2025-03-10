package ru.ikrom.library.favorite_playlists

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import ru.ikrom.library.R

class CreatePlaylistDialog : DialogFragment() {

    interface CreatePlaylistListener {
        fun onCreatePlaylist(name: String, imageUrl: String?)
    }

    private var listener: CreatePlaylistListener? = null

    fun setListener(listener: CreatePlaylistListener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_create_playlist, null)

        val etName = view.findViewById<TextInputEditText>(R.id.etPlaylistName)
        val etUrl = view.findViewById<TextInputEditText>(R.id.etImageUrl)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle("Создать плейлист")
            .setView(view)
            .setPositiveButton("Создать") { _, _ ->
                val name = etName.text?.toString()?.trim()
                val url = etUrl.text?.toString()?.trim()

                if (name.isNullOrEmpty()) {
                    etName.error = "Введите название"
                    return@setPositiveButton
                }

                listener?.onCreatePlaylist(name, url.takeIf { !it.isNullOrEmpty() })
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog?.dismiss()
            }
            .create()
    }
}