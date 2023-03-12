package com.summer.passwordmanager.ui.screens.main.frags

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragMainPassGeneratorBinding
import com.summer.passwordmanager.ui.screens.main.models.PassGeneratorModel
import com.summer.passwordmanager.utils.Utils

class PassGeneratorFrag : BaseFragment<FragMainPassGeneratorBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_main_pass_generator

    private val passGeneratorModel = PassGeneratorModel()

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding?.model = passGeneratorModel
        setPassText()
        listeners()
    }

    private fun listeners() {
        mBinding?.run {
            ivFragPassGeneratorRefresh.setOnClickListener {
                setPassText()
            }
            ivFragPassGeneratorCopy.setOnClickListener {
                (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?)?.setPrimaryClip(
                    ClipData.newPlainText(
                        "Copy", tvFragPassGeneratorPassHolder.text.toString()
                    )
                )
                Toast.makeText(
                    requireContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT
                ).show()
            }
            sbFragPassGeneratorLength.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekbar: SeekBar?, value: Int, progressChanged: Boolean
                ) {
                    passGeneratorModel.length = value
                    passGeneratorModel.notifyChange()
                    if (progressChanged) {
                        setPassText()
                    }
                }

                override fun onStartTrackingTouch(seekbar: SeekBar?) {}

                override fun onStopTrackingTouch(seekbar: SeekBar?) {}

            })
            swFragPassGeneratorLowerAlphas.setOnCheckedChangeListener { _, _ ->
                passGeneratorModel.hasLowerAlphas = swFragPassGeneratorLowerAlphas.isChecked
                passGeneratorModel.notifyChange()
                setPassText()
            }
            swFragPassGeneratorUpperAlphas.setOnCheckedChangeListener { _, _ ->
                passGeneratorModel.hasUpperAlphas = swFragPassGeneratorUpperAlphas.isChecked
                passGeneratorModel.notifyChange()
                setPassText()
            }
            swFragPassGeneratorNumbers.setOnCheckedChangeListener { _, _ ->
                passGeneratorModel.hasNumbers = swFragPassGeneratorNumbers.isChecked
                passGeneratorModel.notifyChange()
                setPassText()
            }
            swFragPassGeneratorSpecialCharacters.setOnCheckedChangeListener { _, _ ->
                passGeneratorModel.hasSpecialCharacters =
                    swFragPassGeneratorSpecialCharacters.isChecked
                passGeneratorModel.notifyChange()
                setPassText()
            }
        }
    }

    private fun setPassText() {
        mBinding?.tvFragPassGeneratorPassHolder?.text =
            SpannableString(
                Utils.getRandomString(
                    passGeneratorModel.length,
                    passGeneratorModel.hasUpperAlphas,
                    passGeneratorModel.hasLowerAlphas,
                    passGeneratorModel.hasNumbers,
                    passGeneratorModel.hasSpecialCharacters
                )
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