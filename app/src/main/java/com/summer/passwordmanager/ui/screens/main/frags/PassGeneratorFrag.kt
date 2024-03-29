package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragMainPassGeneratorBinding
import com.summer.passwordmanager.ui.screens.main.viewmodels.CreateVaultViewModel
import com.summer.passwordmanager.ui.screens.main.viewmodels.PassGeneratorViewModel
import com.summer.passwordmanager.utils.AppUtils
import com.summer.passwordmanager.utils.extensions.showShortToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PassGeneratorFrag : BaseFragment<FragMainPassGeneratorBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_main_pass_generator

    private val viewModel: PassGeneratorViewModel by viewModel()
    private val createVaultViewModel: CreateVaultViewModel by activityViewModel()

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding.model = viewModel.passGeneratorModel
        setPassText()
        listeners()
        mBinding.clFragPassGeneratorContainer.clLayoutCancelSaveContainer.isVisible =
            arguments?.getBoolean("fetchPass") ?: false
    }

    private fun listeners() {
        mBinding.run {
            ivFragPassGeneratorRefresh.setOnClickListener {
                setPassText()
            }
            ivFragPassGeneratorCopy.setOnClickListener {
                AppUtils.copyText(context, tvFragPassGeneratorPassHolder.text.toString())
                showShortToast(getString(R.string.copied_to_clipboard))
            }
            sbFragPassGeneratorLength.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?, value: Int, progressChanged: Boolean
                ) {
                    viewModel.passGeneratorModel.length = value
                    viewModel.passGeneratorModel.notifyChange()
                    if (progressChanged) {
                        setPassText()
                    }
                }

                override fun onStartTrackingTouch(seekbar: SeekBar?) {}

                override fun onStopTrackingTouch(seekbar: SeekBar?) {}

            })
            swFragPassGeneratorLowerAlphas.setOnCheckedChangeListener { _, _ ->
                viewModel.passGeneratorModel.hasLowerAlphas =
                    swFragPassGeneratorLowerAlphas.isChecked
                viewModel.passGeneratorModel.notifyChange()
                setPassText()
            }
            swFragPassGeneratorUpperAlphas.setOnCheckedChangeListener { _, _ ->
                viewModel.passGeneratorModel.hasUpperAlphas =
                    swFragPassGeneratorUpperAlphas.isChecked
                viewModel.passGeneratorModel.notifyChange()
                setPassText()
            }
            swFragPassGeneratorNumbers.setOnCheckedChangeListener { _, _ ->
                viewModel.passGeneratorModel.hasNumbers = swFragPassGeneratorNumbers.isChecked
                viewModel.passGeneratorModel.notifyChange()
                setPassText()
            }
            swFragPassGeneratorSpecialCharacters.setOnCheckedChangeListener { _, _ ->
                viewModel.passGeneratorModel.hasSpecialCharacters =
                    swFragPassGeneratorSpecialCharacters.isChecked
                viewModel.passGeneratorModel.notifyChange()
                setPassText()
            }
            clFragPassGeneratorContainer.tvLayoutCancelSaveCancelButton.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.passGeneratorFrag) {
                    findNavController().navigateUp()
                }
            }
            clFragPassGeneratorContainer.tvLayoutCancelSaveConfirmButton.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.passGeneratorFrag) {
                    createVaultViewModel.passwordEditTextModel.editTextContent =
                        tvFragPassGeneratorPassHolder.text.toString()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun setPassText() {
        mBinding.tvFragPassGeneratorPassHolder.text =
            SpannableString(
                AppUtils.getRandomString(
                    viewModel.passGeneratorModel.length,
                    viewModel.passGeneratorModel.hasUpperAlphas,
                    viewModel.passGeneratorModel.hasLowerAlphas,
                    viewModel.passGeneratorModel.hasNumbers,
                    viewModel.passGeneratorModel.hasSpecialCharacters
                ).also {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.insertPassHistory(viewModel.buildPassHistoryModel(it))
                    }
                }
            ).apply {
                for (i in 0 until this.toString().length) {
                    if (this.toString()[i].isDigit()) {
                        setSpan(
                            ForegroundColorSpan(
                                ContextCompat.getColor(
                                    requireContext(), R.color.blue_light
                                )
                            ), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    } else if (this.toString()[i] in "@#$%^&*") {
                        setSpan(
                            ForegroundColorSpan(
                                ContextCompat.getColor(
                                    requireContext(), R.color.red_light
                                )
                            ), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                    }
                }
            }
    }

}