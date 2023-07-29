package com.summer.passwordmanager.ui.screens.main.frags

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragFileImportDetailsBinding
import com.summer.passwordmanager.ui.screens.main.viewmodels.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FileImportDetailsFrag : BaseFragment<FragFileImportDetailsBinding>() {

    private val viewModel: ProfileViewModel by activityViewModel()
    private var fileData: Uri? = null
    private val acc = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.data?.apply {
                fileData = this
            }
        }
    }
    override val layoutResId: Int
        get() = R.layout.frag_file_import_details

    override fun onFragmentReady(instanceState: Bundle?) {
        viewModel.key.reset()
        viewModel.fileName.reset()
        mBinding?.model = viewModel
        mBinding?.importFileBtn?.setOnClickListener {
            var filePickerIntent = Intent(Intent.ACTION_GET_CONTENT)
            filePickerIntent.type = "text/plain"
            filePickerIntent =
                Intent.createChooser(filePickerIntent, getString(R.string.import_file))
            acc.launch(filePickerIntent)
        }

        mBinding?.importOkBtn?.setOnClickListener {
            fileData?.let { uri ->
                requireContext().contentResolver.openInputStream(uri)?.let { inputStream ->
                    viewModel.importFile(inputStream)
                    findNavController().navigateUp()
                }
            }
        }
    }

}