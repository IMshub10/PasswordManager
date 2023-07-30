package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import android.os.Environment
import androidx.navigation.fragment.findNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragFileExportDetailsBinding
import com.summer.passwordmanager.ui.screens.main.viewmodels.ProfileViewModel
import com.summer.passwordmanager.utils.AppUtils
import com.summer.passwordmanager.utils.extensions.showShortToast
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import java.io.File

class FileExportDetailsFrag : BaseFragment<FragFileExportDetailsBinding>() {

    private val viewModel: ProfileViewModel by activityViewModel()
    override val layoutResId: Int
        get() = R.layout.frag_file_export_details

    override fun onFragmentReady(instanceState: Bundle?) {
        viewModel.key.reset()
        viewModel.fileName.reset()
        mBinding?.model = viewModel
        mBinding?.exportCopyBtn?.setOnClickListener {
            AppUtils.copyText(requireContext(), viewModel.key.editTextContent ?: "")
            showShortToast(getString(R.string.copied_to_clipboard))
        }

        mBinding?.exportGenerateKeyBtn?.setOnClickListener {
            viewModel.generateKey()
        }

        mBinding?.exportBtn?.setOnClickListener {
            if (viewModel.key.validate() && viewModel.fileName.validate()) {
                val appName = AppUtils.getAppName(requireContext())
                viewModel.exportFile(appName)
                showShortToast(
                    "File saved : ${
                        Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS
                        ).path + File.separator + appName + File.separator + viewModel.fileName.editTextContent + ".txt"
                    }"
                )
                findNavController().navigateUp()
            } else {
                showShortToast(getString(R.string.invalid_input))
            }
        }
    }

}