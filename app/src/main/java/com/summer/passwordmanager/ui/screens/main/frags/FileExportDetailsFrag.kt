package com.summer.passwordmanager.ui.screens.main.frags

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragFileExportDetailsBinding
import com.summer.passwordmanager.ui.screens.main.viewmodels.ProfileViewModel
import com.summer.passwordmanager.utils.AppUtils
import com.summer.passwordmanager.utils.extensions.showShortToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FileExportDetailsFrag : BaseFragment<FragFileExportDetailsBinding>() {

    private val viewModel: ProfileViewModel by activityViewModel()
    override val layoutResId: Int
        get() = R.layout.frag_file_export_details

    private var createFileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult? ->
        if (result?.resultCode == Activity.RESULT_OK) {
            result.data?.data
                ?.let { uri ->
                    try {
                        val fileOutputStream =
                            requireContext().contentResolver.openOutputStream(uri)!!
                        lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.exportFile(
                                AppUtils.getAppName(requireContext()),
                                fileOutputStream = fileOutputStream
                            )
                            showShortToast(getString(R.string.file_exported_successfully))
                            findNavController().navigateUp()
                        }
                    } catch (e: Exception) {
                        showShortToast(getString(R.string.unable_to_export_file))
                        e.printStackTrace()
                        FirebaseCrashlytics.getInstance().recordException(e)
                    }
                } ?: showShortToast(getString(R.string.unable_to_export_file))
        }
    }

    override fun onFragmentReady(instanceState: Bundle?) {
        viewModel.key.reset()
        mBinding.model = viewModel
        mBinding.exportCopyBtn.setOnClickListener {
            AppUtils.copyText(requireContext(), viewModel.key.editTextContent.orEmpty())
            showShortToast(getString(R.string.copied_to_clipboard))
        }

        mBinding.exportGenerateKeyBtn.setOnClickListener {
            viewModel.generateKey()
        }

        mBinding.exportBtn.setOnClickListener {
            if (viewModel.key.validate())
                createFileLauncher.launch(
                    Intent(Intent.ACTION_CREATE_DOCUMENT)
                        .setType("text/plain")
                        .addCategory(Intent.CATEGORY_OPENABLE)
                )
            else
                showShortToast(getString(R.string.invalid_input))
        }
    }
}