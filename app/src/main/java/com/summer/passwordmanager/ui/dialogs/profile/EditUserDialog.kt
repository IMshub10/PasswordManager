package com.summer.passwordmanager.ui.dialogs.profile

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseDialogFragment
import com.summer.passwordmanager.databinding.DialogEditProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditUserDialog(private val dismissListener: DismissListener) :
    BaseDialogFragment<DialogEditProfileBinding>() {

    override val layoutResId: Int
        get() = R.layout.dialog_edit_profile

    private val viewModel: UserViewModel by viewModel()

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding.model = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
    }

    private fun listeners() {
        with(mBinding) {
            tvDialogEditProfileCancelButton.setOnClickListener {
                dismiss()
            }
            tvDialogEditProfileConfirmButton.setOnClickListener {
                if (validate()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.saveUserNameMobile()
                        withContext(Dispatchers.Main) {
                            dismiss()
                            dismissListener.onComplete()
                        }
                    }
                }
            }
        }
    }

    interface DismissListener {
        fun onComplete()
    }

    private fun validate(): Boolean {
        if (!viewModel.fullNameEditTextModel.validate()) {
            mBinding.etDialogEditProfileUserName.tilEditTextEdit.error =
                getString(R.string.invalid_input)
            return false
        }
        return true
    }
}