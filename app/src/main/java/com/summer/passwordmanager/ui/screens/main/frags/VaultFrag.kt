package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragMainVaultBinding
import com.summer.passwordmanager.ui.screens.main.viewmodels.CreateVaultViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class VaultFrag : BaseFragment<FragMainVaultBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_main_vault

    private val viewModel: CreateVaultViewModel by activityViewModel()

    override fun onFragmentReady(instanceState: Bundle?) {
        listeners()
    }

    private fun listeners() {
        mBinding?.run {
            fabFragMainVaultAdd.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.vaultFrag) {
                    viewModel.resetUiModels()
                    findNavController().navigate(R.id.action_vaultFrag_to_createVaultFrag)
                }
            }
        }
    }
}