package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragMainProfileBinding
import com.summer.passwordmanager.ui.screens.main.viewmodels.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class ProfileFrag : BaseFragment<FragMainProfileBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_main_profile

    private val viewModel: ProfileViewModel by activityViewModel()

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding?.userModel = viewModel.userModel
        listeners()
    }

    private fun listeners() {
        mBinding?.run {
            layoutFragMainProfileExportFile.clLayoutIconTextButtonRoot.setOnClickListener {
                findNavController().navigate(R.id.action_profileFrag_to_fileExportDetailsFrag)
            }

            layoutFragMainProfileImportFile.clLayoutIconTextButtonRoot.setOnClickListener {
                findNavController().navigate(R.id.action_profileFrag_to_fileImportDetailsFrag)
            }
        }
    }

}