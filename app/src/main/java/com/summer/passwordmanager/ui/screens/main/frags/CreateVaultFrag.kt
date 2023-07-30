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
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.databinding.FragCreateVaultBinding
import com.summer.passwordmanager.ui.adapters.SelectTagAdapter
import com.summer.passwordmanager.ui.dialogs.CreateTagDialog
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

    private var adapter: SelectTagAdapter? = null

    private var createTagDialog: CreateTagDialog? = null

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
            clFragCreateVaultContainer.tvLayoutCancelSaveCancelButton.setOnClickListener {
                if (findNavController().currentDestination?.id == R.id.createVaultFrag) {
                    findNavController().navigateUp()
                }
            }
            clFragCreateVaultContainer.tvLayoutCancelSaveConfirmButton.setOnClickListener {
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
        viewModel.getAllTags().observe(viewLifecycleOwner) {
            adapter?.submitList(it?.toMutableList()?.apply {
                viewModel.selectedTagId?.let { selectedId ->
                    this.find { selected -> selected.id == selectedId }?.apply {
                        isSelected = true
                        notifyChange()
                    }
                }
                this.add(
                    this.size,
                    TagEntity(
                        id = AppUtils.KEY_ADD,
                        createdAtApp = 0,
                        updatedAtApp = 0,
                        name = "+ Add Tag",
                        description = null
                    )
                )
            }?.toList() ?: listOf())
        }
    }

    private fun initRecyclerView() {
        mBinding?.run {
            (rvFragCreateVaultTags.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
                false
            rvFragCreateVaultTags.layoutManager = FlexboxLayoutManager(requireContext()).apply {
                flexDirection = FlexDirection.ROW
                justifyContent = JustifyContent.FLEX_START
            }
            adapter =
                SelectTagAdapter(object : SelectTagAdapter.SelectionCallBack {
                    override fun onItemClick(item: TagEntity) {
                        if (item.isSelected) {
                            viewModel.selectedTagId = null
                            item.isSelected = false
                            item.notifyChange()
                        } else {
                            if (item.id == AppUtils.KEY_ADD) {
                                viewModel.selectedTagId = null
                                adapter?.currentList?.forEach {
                                    it.isSelected = false
                                    it.notifyChange()
                                }
                                showCreateTagDialog()
                            } else {
                                viewModel.selectedTagId = item.id
                                adapter?.currentList?.forEach {
                                    it.isSelected = it.id == item.id
                                    it.notifyChange()
                                }
                            }
                        }
                    }
                })
            this.rvFragCreateVaultTags.adapter = adapter
        }
    }

    private fun showCreateTagDialog() {
        if (createTagDialog?.isAdded != true) {
            createTagDialog = CreateTagDialog(object : CreateTagDialog.DismissLister {
                override fun onSave(tagName: String, descriptionName: String?) {
                    lifecycleScope.launch(Dispatchers.Default) {
                        viewModel.insertTagEntity(
                            TagEntity(
                                name = tagName,
                                description = descriptionName
                            ).apply {
                                id = AppUtils.generateXid()
                                createdAtApp = AppUtils.getCurrentTimeSecs()
                                updatedAtApp = AppUtils.getCurrentTimeSecs()
                            }
                        )
                    }
                }
            })
            createTagDialog?.show(childFragmentManager, "")
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