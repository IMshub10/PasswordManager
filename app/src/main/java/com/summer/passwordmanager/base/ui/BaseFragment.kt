package com.summer.passwordmanager.base.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<B : ViewDataBinding> : Fragment() {

    @get:LayoutRes
    protected abstract val layoutResId: Int

    var mBinding: B? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                layoutResId,
                container,
                false
            )
        onFragmentReady(savedInstanceState)
        return mBinding?.root
    }

    protected abstract fun onFragmentReady(instanceState: Bundle?)

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding = null
    }

}