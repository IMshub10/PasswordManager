package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.summer.passwordmanager.databinding.FragOptionsBinding

class OptionsFrag<T>(private val dismissListener: DismissListener<T>) :
    BottomSheetDialogFragment() {

    private var model: T? = null

    fun setModel(model: T) {
        this.model = model
    }

    private var binding: FragOptionsBinding? = null
    private val mBinding:FragOptionsBinding
        get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragOptionsBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.layoutFragVaultOptionsViewEdit.clLayoutIconTextButtonRoot.setOnClickListener {
            dismiss()
            model?.let {
                dismissListener.onEdit(it)
            }
        }
        mBinding.layoutFragVaultOptionsDelete.clLayoutIconTextButtonRoot.setOnClickListener {
            dismiss()
            model?.let {
                dismissListener.onDelete(it)
            }
        }
    }

    interface DismissListener<T> {
        fun onDelete(model: T)
        fun onEdit(model: T)
    }

}