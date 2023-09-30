package com.summer.passwordmanager.base.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.summer.passwordmanager.R
import com.summer.passwordmanager.utils.UiUtils


abstract class BaseDialogFragment<B : ViewDataBinding> : DialogFragment() {

    @get:LayoutRes
    protected abstract val layoutResId: Int

    private var binding: B? = null
    protected val mBinding: B
        get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        binding =
            DataBindingUtil.inflate(
                inflater,
                layoutResId,
                container,
                false
            )
        onFragmentReady(savedInstanceState)
        return mBinding.root
    }

    protected abstract fun onFragmentReady(instanceState: Bundle?)

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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
}