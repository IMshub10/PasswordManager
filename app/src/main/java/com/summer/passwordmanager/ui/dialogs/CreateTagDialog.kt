package com.summer.passwordmanager.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.summer.passwordmanager.R
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.databinding.DialogCreateTagBinding
import com.summer.passwordmanager.utils.UiUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateTagDialog(
    private val dismissListener: DismissLister,
    private val tagEntity: TagEntity? = null
) : DialogFragment() {

    private var mBinding: DialogCreateTagBinding? = null
    private val binding: DialogCreateTagBinding
        get() = mBinding!!

    private val viewModel: CreateTagViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.tagEntity = tagEntity
        viewModel.setUpInputModels()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        isCancelable = false
        mBinding = DialogCreateTagBinding.inflate(layoutInflater)
        binding.model = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
    }

    private fun listeners() {
        with(binding) {
            tvDialogCreateTagCancelButton.setOnClickListener {
                dismiss()
            }
            tvDialogCreateTagConfirmButton.setOnClickListener {
                if (validate()) {
                    dismiss()
                    dismissListener.onSave(
                        viewModel.toSaveTagEntity()
                    )
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (!viewModel.tagNameEditTextModel.validate()) {
            mBinding?.etDialogCreateTagTagName?.tilEditTextEdit?.error =
                getString(R.string.invalid_input)
            return false
        }
        if (!viewModel.tagDescriptionEditTextModel.validate()) {
            mBinding?.etDialogCreateTagTagDescription?.tilEditTextEdit?.error =
                getString(R.string.invalid_input)
            return false
        }
        return true
    }

    interface DismissLister {
        fun onSave(tagEntity: TagEntity)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = Dialog(requireActivity()).apply {
        window?.attributes?.windowAnimations = R.style.SpannableDialogAnimation
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(RelativeLayout(requireActivity()).apply {
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), android.R.color.transparent
                )
            )
            layoutParams = ViewGroup.LayoutParams(
                UiUtils.getScreenWidthIntDp(requireActivity()) - 160,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            UiUtils.getScreenWidthIntDp(requireActivity()) - 128,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }
}