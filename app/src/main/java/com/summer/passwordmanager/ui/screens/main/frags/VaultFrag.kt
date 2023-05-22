package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.database.entities.FolderEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.databinding.FragMainVaultBinding
import com.summer.passwordmanager.ui.adapters.ViewFolderAdapter
import com.summer.passwordmanager.ui.adapters.ViewVaultAdapter
import com.summer.passwordmanager.ui.screens.main.viewmodels.CreateVaultViewModel
import com.summer.passwordmanager.ui.screens.main.viewmodels.VaultViewModel
import com.summer.passwordmanager.utils.AppUtils
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class VaultFrag : BaseFragment<FragMainVaultBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_main_vault

    private val viewModel: CreateVaultViewModel by activityViewModel()
    private val mainViewModel: VaultViewModel by viewModel()
    private lateinit var adapter: ViewVaultAdapter

    override fun onFragmentReady(instanceState: Bundle?) {
        initRecyclerView()
        listeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        mainViewModel.getAllVaultsWithTheirFolder().observe(viewLifecycleOwner) {
            it?.let {
                adapter.submitList(it.toList().map { pair ->
                    pair.first.apply {
                        folderEntity = pair.second
                    }
                })
            }
        }
    }

    private fun initRecyclerView() {
        mBinding?.run {
            (rvFragVaultVaults.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            adapter = ViewVaultAdapter(object : ViewVaultAdapter.SelectionCallBack {
                override fun onItemClick(item: VaultEntity) {

                }
            })
            this.rvFragVaultVaults.adapter = adapter
        }
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