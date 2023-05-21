package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateVaultFrag : BaseFragment<FragCreateVaultBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_create_vault

    private val viewModel: CreateVaultViewModel by viewModel()

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
        }
    }

    private fun observeViewModel() {
        viewModel.getAllFolders().observe(viewLifecycleOwner) {
            adapter?.submitList(it ?: listOf())
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

                    }
                })
            this.rvFragCreateVaultFolders.adapter = adapter
        }
    }
}