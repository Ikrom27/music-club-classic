package ru.ikrom.companion_connect.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.ikrom.companion_connect.R
import javax.inject.Inject

@AndroidEntryPoint
class QRCodeScanner : Fragment(R.layout.fragment_qrcode_scanner) {
    private lateinit var codeScanner: CodeScanner
    private val viewModel: ScannerViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        codeScanner = CodeScanner(requireContext(), view.findViewById(R.id.scanner_view))
        codeScanner.decodeCallback = DecodeCallback { data ->
            viewModel.saveCompanionData(data.text)
            val (ip, port) = data.text.split(":")
            viewModel.connectTo(ip, port.toInt())
            CoroutineScope(Dispatchers.Main).launch {
                findNavController().navigateUp()
            }
        }
        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                codeScanner.startPreview()
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            ) -> {
                showRationaleDialog()
            }

            else -> {
                requestCameraPermission()
            }
        }
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_CODE
        )
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Доступ к камере")
            .setMessage("Приложению требуется доступ к камере для работы")
            .setPositiveButton("Дать доступ") { _, _ ->
                requestCameraPermission()
            }
            .setNegativeButton("Отмена") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    codeScanner.startPreview()
                } else {
                    handlePermissionDenied()
                }
            }
        }
    }

    private fun handlePermissionDenied() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            )) {
            showSettingsDialog()
        } else {
            Toast.makeText(
                requireContext(),
                "Доступ к камере отклонен",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Требуется доступ")
            .setMessage("Перейдите в настройки и предоставьте разрешение")
            .setPositiveButton("Настройки") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Закрыть", null)
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        startActivity(intent)
        codeScanner.startPreview()
    }

    companion object {
        private const val CAMERA_PERMISSION_CODE = 101
    }
}