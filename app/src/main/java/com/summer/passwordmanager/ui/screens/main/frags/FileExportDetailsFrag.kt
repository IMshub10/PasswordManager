package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragFileExportDetailsBinding
import com.summer.passwordmanager.ui.screens.main.viewmodels.ProfileViewModel
import com.summer.passwordmanager.utils.AppUtils
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
            Toast.makeText(
                requireContext(), "copied", Toast.LENGTH_SHORT
            ).show()
        }

        mBinding?.exportGenerateKeyBtn?.setOnClickListener {
            viewModel.generateKey()
        }

        mBinding?.exportBtn?.setOnClickListener {
            if (viewModel.key.validate() && viewModel.fileName.validate()) {
                val applicationInfo = requireContext().applicationInfo
                val stringId = applicationInfo.labelRes
                val appName = if (stringId == 0) {
                    applicationInfo.nonLocalizedLabel.toString()
                } else {
                    getString(stringId)
                }
                viewModel.exportFile(appName)
                Toast.makeText(
                    requireContext(), "File saved : ${
                        Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS
                        ).path + File.separator + appName + File.separator + viewModel.fileName.editTextContent + ".txt"
                    }", Toast.LENGTH_SHORT
                ).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.invalid_input),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}