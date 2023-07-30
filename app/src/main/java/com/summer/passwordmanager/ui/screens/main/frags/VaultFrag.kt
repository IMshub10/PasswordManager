package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.SimpleItemAnimator
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseActivity
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.databinding.FragMainVaultBinding
import com.summer.passwordmanager.ui.adapters.ViewTagAdapter
import com.summer.passwordmanager.ui.adapters.ViewVaultAdapter
import com.summer.passwordmanager.ui.dialogs.HelperAlertDialog
import com.summer.passwordmanager.ui.screens.main.viewmodels.CreateVaultViewModel
import com.summer.passwordmanager.ui.screens.main.viewmodels.VaultViewModel
import com.summer.passwordmanager.utils.AppUtils
import com.summer.passwordmanager.utils.extensions.showShortToast
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class VaultFrag : BaseFragment<FragMainVaultBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_main_vault

    private val viewModel: CreateVaultViewModel by activityViewModel()
    private val mainViewModel: VaultViewModel by activityViewModel()

    private var viewVaultAdapter: ViewVaultAdapter? = null
    private var viewTagAdapter: ViewTagAdapter? = null

    private val optionsFrag by lazy {
        OptionsFrag(dismissListener = object :
            OptionsFrag.DismissListener<VaultEntity> {
            override fun onDelete(model: VaultEntity) {
                (activity as BaseActivity<*>).run {
                    initHelperDialog(dialogType = HelperAlertDialog.DialogType.TWO_BUTTON)
                    helperDialog?.run {
                        setTitleText(this@VaultFrag.getString(R.string.delete))
                        setContentText(this@VaultFrag.getString(R.string.are_you_sure_you_want_to_delete))
                        setCancelClickListener {
                            dismiss()
                        }
                        setConfirmClickListener {
                            dismiss()
                            mainViewModel.deleteVaultById(model.id)
                        }
                    }
                }
            }

            override fun onEdit(model: VaultEntity) {
                if (findNavController().currentDestination?.id == R.id.vaultFrag) {
                    findNavController().navigate(
                        R.id.action_vaultFrag_to_createVaultFrag,
                        args = Bundle().apply {
                            putString("vaultId", model.id)
                        })
                }
            }

        })
    }

    override fun onFragmentReady(instanceState: Bundle?) {
        initRecyclerView()
        listeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        mainViewModel.getFilteredVaultList().observe(viewLifecycleOwner) {
            it?.let {
                viewVaultAdapter?.submitList(it)
            }
        }
        mainViewModel.tagListLive.observe(viewLifecycleOwner) {
            mBinding?.rvFragVaultTags?.isVisible = if (it == null) false else it.size > 1
            viewTagAdapter?.submitList(
                it ?: listOf()
            )
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
                    item.isHidden = !item.isHidden
                    item.notifyChange()
                }

                override fun onLongPress(item: VaultEntity) {
                    optionsFrag.setModel(model = item)
                    optionsFrag.show(childFragmentManager, null)
                }

                override fun onPassVisibilityClick(item: VaultEntity) {
                    item.passwordVisible = !item.passwordVisible
                    item.notifyChange()
                }

                override fun onCopyPas(item: VaultEntity) {
                    AppUtils.copyText(context, item.password)
                    showShortToast(getString(R.string.copied_to_clipboard))

                }
            })
            this.rvFragVaultVaults.adapter = viewVaultAdapter
        }
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