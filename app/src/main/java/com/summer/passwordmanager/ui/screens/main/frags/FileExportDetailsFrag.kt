package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

    override fun onFragmentReady(instanceState: Bundle?) {
        viewModel.key.reset()
        viewModel.fileName.reset()
        mBinding.model = viewModel
        mBinding.exportCopyBtn.setOnClickListener {
            AppUtils.copyText(requireContext(), viewModel.key.editTextContent ?: "")
            showShortToast(getString(R.string.copied_to_clipboard))
        }

        mBinding.exportGenerateKeyBtn.setOnClickListener {
            viewModel.generateKey()
        }

        mBinding.exportBtn.setOnClickListener {
            if (viewModel.key.validate() && viewModel.fileName.validate()) {
                lifecycleScope.launch(Dispatchers.Main) {
                    val fileName = viewModel.exportFile(AppUtils.getAppName(requireContext()))
                    if (fileName != null) {
                        findNavController().navigateUp()
                        showShortToast(
                            "File saved : $fileName"
                        )
                    } else
                        showShortToast("Unable to export file.")
                }

            } else {
                showShortToast(getString(R.string.invalid_input))
            }
        }
    }
}