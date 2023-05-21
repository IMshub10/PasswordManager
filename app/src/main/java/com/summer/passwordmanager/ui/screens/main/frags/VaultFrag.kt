package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragMainVaultBinding

class VaultFrag : BaseFragment<FragMainVaultBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_main_vault

    override fun onFragmentReady(instanceState: Bundle?) {
        listeners()
    }

    private fun listeners() {
        mBinding?.run {
            fabFragMainVaultAdd.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.vaultFrag) {
                    findNavController().navigate(R.id.action_vaultFrag_to_createVaultFrag)
                }
            }
        }
    }
}