package com.summer.passwordmanager.ui.screens.pin

import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.hbisoft.pinlockr.PinlockrListener
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseActivity
import com.summer.passwordmanager.databinding.ActivityPinBinding
import com.summer.passwordmanager.ui.screens.main.MainActivity
import com.summer.passwordmanager.ui.screens.pin.viewmodels.PinViewModel
import com.summer.passwordmanager.utils.AppUtils.PIN_LENGTH
import com.summer.passwordmanager.utils.BiometricResultListener
import com.summer.passwordmanager.utils.LauncherUtils
import com.summer.passwordmanager.utils.extensions.showShortToast
import com.summer.passwordmanager.utils.verifyFingerPrint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PinActivity : BaseActivity<ActivityPinBinding>(), BiometricResultListener, PinlockrListener {

    override val layoutResId: Int
        get() = R.layout.activity_pin

    private val viewModel: PinViewModel by viewModel()

    override fun onActivityReady(savedInstanceState: Bundle?) {
        mBinding.model = viewModel
        viewModel.init()
        mBinding.fragPinUseLock.setupPinlockrView(
            this, this, mBinding.fragPinIndicatorDots, PIN_LENGTH
        )
        listeners()
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isFingerPrintEnabled.collectLatest {
                    if (it) {
                        verifyFingerPrint(this@PinActivity)
                    }
                }
            }
        }
    }

    private fun listeners() {
        mBinding.tvActivityPinFingerPrint.setOnClickListener {
            verifyFingerPrint(this)
        }
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onBiometricSuccess(result: BiometricPrompt.AuthenticationResult) =
        LauncherUtils.startActivityWithClearTop(
            this@PinActivity,
            MainActivity::class.java
        )

    override fun onBiometricError(message: String) = showShortToast(message)

    override fun onComplete(pin: String?) {
        if (pin == null) return
        if (pin.length != PIN_LENGTH) return
        if (pin == viewModel.pin) {
            LauncherUtils.startActivityWithClearTop(
                this@PinActivity,
                MainActivity::class.java
            )
        } else showShortToast(getString(R.string.invalid_pin))

    }

    override fun onBiometricSuccess() {}

    override fun onBiometricFailed(reason: Int) {}

}