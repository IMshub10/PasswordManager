package com.summer.passwordmanager.ui.screens.pin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.andrognito.pinlockview.PinLockListener
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseActivity
import com.summer.passwordmanager.databinding.ActivityPinBinding
import com.summer.passwordmanager.ui.screens.main.MainActivity
import com.summer.passwordmanager.ui.screens.pin.viewmodels.PinViewModel
import com.summer.passwordmanager.utils.LauncherUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinActivity : BaseActivity<ActivityPinBinding>() {

    override val layoutResId: Int
        get() = R.layout.activity_pin

    private val viewModel: PinViewModel by viewModel()

    override fun onActivityReady(savedInstanceState: Bundle?) {
        mBinding.model = viewModel
        viewModel.init()
        mBinding.fragPinUseLock.attachIndicatorDots(mBinding.fragPinIndicatorDots)
        listeners()
    }

    private fun listeners() {
        mBinding.tvActivityPinFingerPrint.setOnClickListener {
            checkAndValidateFingerPrint()
        }
        mBinding.fragPinUseLock.setPinLockListener(object : PinLockListener {
            override fun onComplete(pin: String?) {
                if (pin == null) return
                if (pin.length != 4) return
                if (pin == viewModel.pin) {
                    LauncherUtils.startActivityWithClearTop(
                        this@PinActivity,
                        MainActivity::class.java
                    )
                } else {
                    Toast.makeText(this@PinActivity, "Invalid Pin", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onEmpty() {

            }

            override fun onPinChange(pinLength: Int, intermediatePin: String?) {

            }

        })
    }

    private fun checkAndValidateFingerPrint() {
        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                BiometricPrompt(this@PinActivity,
                    ContextCompat.getMainExecutor(this),
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence
                        ) {
                            super.onAuthenticationError(errorCode, errString)
                            Toast.makeText(
                                this@PinActivity,
                                "Authentication error: $errString", Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                        override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult
                        ) {
                            super.onAuthenticationSucceeded(result)
                            LauncherUtils.startActivityWithClearTop(
                                this@PinActivity,
                                MainActivity::class.java
                            )
                        }

                        override fun onAuthenticationFailed() {
                            super.onAuthenticationFailed()
                            Toast.makeText(
                                this@PinActivity, "Authentication failed",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }).authenticate(
                    BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Authenticate to Password-Manager")
                        .setSubtitle("Verify identify.")
                        .setNegativeButtonText("Cancel")
                        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
                        .build()
                )
            }

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                Log.d("PinActivity", "No biometric features available on this device.")
            }

            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                Log.d("PinActivity", "Biometric features are currently unavailable.")
            }

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                Log.e("PinActivity", "Biometric non enrolled.")
                Toast.makeText(
                    this,
                    "No enrolled fingerprints found, please go to settings and enroll fingerprint.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }
}