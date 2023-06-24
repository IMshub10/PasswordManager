package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragMainProfileBinding
import com.summer.passwordmanager.ui.screens.main.viewmodels.PassGeneratorViewModel
import com.summer.passwordmanager.ui.screens.main.viewmodels.ProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFrag : BaseFragment<FragMainProfileBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_main_profile

    private val viewModel: ProfileViewModel by viewModel()

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding?.userModel = viewModel.userModel
    }

}