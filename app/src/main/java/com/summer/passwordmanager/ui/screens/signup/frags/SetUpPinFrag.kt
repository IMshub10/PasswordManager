package com.summer.passwordmanager.ui.screens.signup.frags

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragSetupPinBinding
import com.summer.passwordmanager.ui.screens.main.MainActivity
import com.summer.passwordmanager.ui.screens.signup.viewmodels.SetUpPinViewModel
import com.summer.passwordmanager.utils.LauncherUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class SetUpPinFrag : BaseFragment<FragSetupPinBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_setup_pin

    private val viewModel: SetUpPinViewModel by viewModel()

    override fun onFragmentReady(instanceState: Bundle?) {
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
                        checkAndValidateFingerPrint()
                    }else{
                        saveAndProceed()
                    }
                }
            }
        }
    }

    private fun checkAndValidateFingerPrint() {
        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                BiometricPrompt(this@SetUpPinFrag,
                    ContextCompat.getMainExecutor(requireContext()),
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            Toast.makeText(
                                requireContext(),
                                "Authentication error: $errString", Toast.LENGTH_SHORT
                            )
                                .show()
                            mBinding?.cbFragSetUpPinEnableFingerPrint?.isChecked = false
                        }

                        override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult
                        ) {
                            super.onAuthenticationSucceeded(result)
                            Toast.makeText(
                                requireContext(),
                                "Authentication succeeded!", Toast.LENGTH_SHORT
                            )
                                .show()
                            saveAndProceed()
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            Toast.makeText(
                                requireContext(), "Authentication failed",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            mBinding?.cbFragSetUpPinEnableFingerPrint?.isChecked = false
                        }
                    }).authenticate(
                    BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Use Pin")
                        .setSubtitle("Use pin to authenticate.")
                        .setNegativeButtonText("Cancel")
                        .setAllowedAuthenticators(BIOMETRIC_STRONG)
                        .build()
                )
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("PinActivity", "No biometric features available on this device.")
                mBinding?.cbFragSetUpPinEnableFingerPrint?.isChecked = false
                mBinding?.cbFragSetUpPinEnableFingerPrint?.isEnabled = false
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d("PinActivity", "Biometric features are currently unavailable.")
                mBinding?.cbFragSetUpPinEnableFingerPrint?.isChecked = false
                mBinding?.cbFragSetUpPinEnableFingerPrint?.isEnabled = false
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e("PinActivity", "Biometric non enrolled.")
                Toast.makeText(
                    requireContext(),
                    "No enrolled fingerprints found, please go to settings and enroll fingerprint.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                mBinding?.cbFragSetUpPinEnableFingerPrint?.isChecked = false
            }
        }
    }

    private fun validate(): Boolean {
        if (viewModel.firstPin?.length != 4) {
            Toast.makeText(requireContext(), "Please enter Pin", Toast.LENGTH_SHORT).show()
            return false
        }
        if (viewModel.secondPin?.length != 4) {
            Toast.makeText(requireContext(), "Please enter Pin", Toast.LENGTH_SHORT).show()
            return false
        }
        if (viewModel.firstPin != viewModel.secondPin) {
            Toast.makeText(requireContext(), "Entered Pin do not match.", Toast.LENGTH_SHORT).show()
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

}