package com.summer.passwordmanager.ui.screens.signup.frags

import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragSetupPinBinding
import com.summer.passwordmanager.ui.screens.main.MainActivity
import com.summer.passwordmanager.ui.screens.signup.viewmodels.SetUpPinViewModel
import com.summer.passwordmanager.utils.BiometricResultListener
import com.summer.passwordmanager.utils.LauncherUtils
import com.summer.passwordmanager.utils.canAuthenticateWithBiometric
import com.summer.passwordmanager.utils.extensions.showShortToast
import com.summer.passwordmanager.utils.verifyFingerPrint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetUpPinFrag : BaseFragment<FragSetupPinBinding>(), BiometricResultListener {

    override val layoutResId: Int
        get() = R.layout.frag_setup_pin

    private val viewModel: SetUpPinViewModel by viewModel()

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding?.cbFragSetUpPinEnableFingerPrint?.isVisible = canAuthenticateWithBiometric()
        listeners()
    }

    private fun listeners() {
        mBinding?.run {
            pvFragSetupPinPin1.addTextChangedListener {
                viewModel.firstPin = it?.toString()
            }
            pvFragSetupPinPin2.addTextChangedListener {
                viewModel.secondPin = it?.toString()
            }
            tvFragSetUpPinSave.setOnClickListener {
                if (validate()) {
                    if (cbFragSetUpPinEnableFingerPrint.isChecked) {
                        verifyFingerPrint(this@SetUpPinFrag)
                    } else {
                        saveAndProceed()
                    }
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (viewModel.firstPin?.length != 4) {
            showShortToast(getString(R.string.please_enter_pin))
            return false
        }
        if (viewModel.secondPin?.length != 4) {
            showShortToast(getString(R.string.please_enter_pin))
            return false
        }
        if (viewModel.firstPin != viewModel.secondPin) {
            showShortToast(getString(R.string.entered_pin_do_not_match))
            return false
        }
        return true
    }

    private fun saveAndProceed() {
        lifecycleScope.launch(Dispatchers.Default) {
            viewModel.save(mBinding?.cbFragSetUpPinEnableFingerPrint?.isChecked)
            withContext(Dispatchers.Main) {
                LauncherUtils.startActivityWithClearTop(
                    requireActivity(),
                    MainActivity::class.java
                )
            }
        }
    }

    override fun onBiometricSuccess(result: BiometricPrompt.AuthenticationResult) =
        saveAndProceed()

    override fun onBiometricError(message: String) =
        showShortToast(message)
}