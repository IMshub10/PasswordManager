package com.summer.passwordmanager.ui.screens.signup.frags

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragSignUpBinding
import com.summer.passwordmanager.ui.screens.signup.viewmodels.SignUpViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFrag : BaseFragment<FragSignUpBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_sign_up

    private val viewModel: SignUpViewModel by viewModel()

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding.model = viewModel
        listeners()
        lifecycleScope.launch(Dispatchers.Default) {
            val accountExists = viewModel.checkUserHasSavedHisName()
            withContext(Dispatchers.Main) {
                if (accountExists) {
                    if (findNavController().currentDestination?.id == R.id.signUpFrag) {
                        findNavController().navigate(R.id.action_signUpFrag_to_setUpPinFrag)
                    }
                }
            }
        }
    }

    private fun listeners() {
        mBinding.run {
            tvFragSignUpSave.setOnClickListener {
                if (validate()) {
                    lifecycleScope.launch(Dispatchers.Default) {
                        viewModel.save()
                        withContext(Dispatchers.Main) {
                            if (findNavController().currentDestination?.id == R.id.signUpFrag) {
                                findNavController().navigate(R.id.action_signUpFrag_to_setUpPinFrag)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (!viewModel.fullNameEditTextModel.validate()) {
            mBinding.etFragSignUpName.tilEditTextEdit.error = getString(R.string.invalid_input)
            return false
        }
        return true
    }
}