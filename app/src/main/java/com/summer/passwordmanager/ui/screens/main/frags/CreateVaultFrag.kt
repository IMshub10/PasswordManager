package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.database.entities.FolderEntity
import com.summer.passwordmanager.databinding.FragCreateVaultBinding
import com.summer.passwordmanager.ui.adapters.ViewFolderAdapter
import com.summer.passwordmanager.ui.screens.main.viewmodels.CreateVaultViewModel
import com.summer.passwordmanager.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CreateVaultFrag : BaseFragment<FragCreateVaultBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_create_vault

    private val viewModel: CreateVaultViewModel by activityViewModel()

    private var adapter: ViewFolderAdapter? = null

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding?.model = viewModel
        initRecyclerView()
        observeViewModel()
        listeners()
    }

    private fun listeners() {
        mBinding?.run {
            tvFragCreateVaultGeneratePassword.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.createVaultFrag) {
                    findNavController().navigate(
                        R.id.action_createVaultFrag_to_passGeneratorFrag,
                        Bundle().apply {
                            putBoolean("fetchPass", true)
                        })
                }
            }
            tvFragCreateVaultCancelButton.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.createVaultFrag) {
                    findNavController().navigateUp()
                }
            }
            tvFragCreateVaultConfirmButton.setOnClickListener {
                if (validate()) {
                    lifecycleScope.launch(Dispatchers.Default) {
                        viewModel.save()
                        withContext(Dispatchers.Main) {
                            if (findNavController().currentDestination?.id == R.id.createVaultFrag) {
                                findNavController().navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.getAllFolders().observe(viewLifecycleOwner) {
            adapter?.submitList(it?.toMutableList()?.apply {
                viewModel.selectedFolderId?.let { selectedId ->
                    this.find { selected -> selected.id == selectedId }?.apply {
                        isSelected = true
                        notifyChange()
                    }
                }
                this.add(this.size, FolderEntity(AppUtils.KEY_ADD, "+ Add Tag", 0, 0))
            }?.toList() ?: listOf())
        }
    }

    private fun initRecyclerView() {
        mBinding?.run {
            (rvFragCreateVaultFolders.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
                false
            rvFragCreateVaultFolders.layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            adapter =
                ViewFolderAdapter(object : ViewFolderAdapter.SelectionCallBack {
                    override fun onItemClick(item: FolderEntity) {
                        if (item.isSelected) {
                            viewModel.selectedFolderId = null
                            item.isSelected = false
                            item.notifyChange()
                        } else {
                            if (item.id == AppUtils.KEY_ADD) {
                                viewModel.selectedFolderId = null
                                adapter?.currentList?.forEach {
                                    it.isSelected = false
                                    it.notifyChange()
                                }
                            } else {
                                viewModel.selectedFolderId = item.id
                                adapter?.currentList?.forEach {
                                    it.isSelected = it.id == item.id
                                    it.notifyChange()
                                }
                            }
                        }
                    }
                })
            this.rvFragCreateVaultFolders.adapter = adapter
        }
    }

    private fun validate(): Boolean {
        if (!viewModel.websiteNameEditTextModel.validate()) {
            mBinding?.etFragCreateVaultEntityName?.tilEditTextEdit?.error =
                getString(R.string.invalid_input)
            return false
        }
        if (!viewModel.websiteAddressEditTextModel.validate()) {
            mBinding?.etFragCreateVaultWebAddress?.tilEditTextEdit?.error =
                getString(R.string.invalid_input)
            return false
        }
        if (!viewModel.userNameMobileEditTextModel.validate()) {
            mBinding?.etFragCreateVaultUserNameMobile?.tilEditTextEdit?.error =
                getString(R.string.invalid_input)
            return false
        }
        if (!viewModel.passwordEditTextModel.validate()) {
            mBinding?.etFragCreateVaultPassword?.tilEditTextEdit?.error =
                getString(R.string.invalid_input)
            return false
        }
        if (!viewModel.notesEditTextModel.validate()) {
            mBinding?.etFragCreateVaultNotes?.tilEditTextEdit?.error =
                getString(R.string.invalid_input)
            return false
        }
        return true
    }
}