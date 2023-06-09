package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.databinding.FragMainVaultBinding
import com.summer.passwordmanager.ui.adapters.ViewTagAdapter
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

    private var viewVaultAdapter: ViewVaultAdapter? = null
    private var viewTagAdapter: ViewTagAdapter? = null

    override fun onFragmentReady(instanceState: Bundle?) {
        initRecyclerView()
        listeners()
        observeViewModel()
        mainViewModel.getAllTags()
    }

    private fun observeViewModel() {
        mainViewModel.getAllVaultsWithTheirTag().observe(viewLifecycleOwner) {
            it?.let {
                viewVaultAdapter?.submitList(it.toList().map { pair ->
                    pair.first.apply {
                        tagEntity = pair.second
                    }
                })
            }
        }
        mainViewModel.tagListLive.observe(viewLifecycleOwner) {
            it?.let {
                viewTagAdapter?.submitList(
                    it
                )
            }
        }
        mainViewModel.selectedTag.observe(viewLifecycleOwner){
        }
    }

    private fun initRecyclerView() {
        mBinding?.run {
            //tag adapter
            (rvFragVaultTags.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            viewTagAdapter = ViewTagAdapter(object : ViewTagAdapter.SelectionCallBack {
                override fun onItemClick(item: TagEntity) {
                    mainViewModel.resetSelectedTag(item)
                }
            })
            this.rvFragVaultTags.adapter = viewTagAdapter

            //vault adapter
            (rvFragVaultVaults.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            viewVaultAdapter = ViewVaultAdapter(object : ViewVaultAdapter.SelectionCallBack {
                override fun onItemClick(item: VaultEntity) {

                }
            })
            this.rvFragVaultVaults.adapter = viewVaultAdapter
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