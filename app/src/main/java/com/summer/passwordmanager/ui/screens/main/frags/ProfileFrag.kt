package com.summer.passwordmanager.ui.screens.main.frags

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.databinding.FragMainProfileBinding
import com.summer.passwordmanager.ui.dialogs.profile.EditUserDialog
import com.summer.passwordmanager.ui.screens.main.viewmodels.ProfileViewModel
import com.summer.passwordmanager.ui.screens.pin.PinActivity
import com.summer.passwordmanager.utils.LauncherUtils
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class ProfileFrag : BaseFragment<FragMainProfileBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_main_profile

    private val viewModel: ProfileViewModel by activityViewModel()

    private var editUserDialog: EditUserDialog? = null

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding.userModel = viewModel.userModel
        listeners()
    }

    private fun showEditProfileDialog() {
        if (editUserDialog?.isAdded != true) {
            editUserDialog =
                EditUserDialog(object : EditUserDialog.DismissListener {
                    override fun onComplete() = viewModel.loadUserProfile()
                })
            editUserDialog?.show(childFragmentManager, "")
        }
    }

    private fun listeners() {
        mBinding.run {
            layoutFragMainProfileTags.clLayoutIconTextButtonRoot.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.profileFrag) {
                    findNavController().navigate(R.id.action_profileFrag_to_tagFrag)
                }
            }
            layoutFragMainProfileExportFile.clLayoutIconTextButtonRoot.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.profileFrag)
                    findNavController().navigate(R.id.action_profileFrag_to_fileExportDetailsFrag)
            }

            layoutFragMainProfileImportFile.clLayoutIconTextButtonRoot.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.profileFrag)
                    findNavController().navigate(R.id.action_profileFrag_to_fileImportDetailsFrag)
            }
            layoutFragMainProfileLockApp.clLayoutIconTextButtonRoot.setOnClickListener {
                LauncherUtils.startActivityWithClearTop(
                    requireActivity(),
                    PinActivity::class.java
                )
            }
            layoutFragMainProfileDataPrivacy.clLayoutIconTextButtonRoot.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/IMshub10/PasswordManager/wiki")))
            }
            tvFragMainProfileMobileEdit.setOnClickListener {
                showEditProfileDialog()
            }
        }
    }

}