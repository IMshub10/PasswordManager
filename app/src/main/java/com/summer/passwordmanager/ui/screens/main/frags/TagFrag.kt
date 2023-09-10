package com.summer.passwordmanager.ui.screens.main.frags

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.SimpleItemAnimator
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseActivity
import com.summer.passwordmanager.base.ui.BaseFragment
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.databinding.FragMainTagBinding
import com.summer.passwordmanager.ui.adapters.TagListAdapter
import com.summer.passwordmanager.ui.dialogs.CreateTagDialog
import com.summer.passwordmanager.ui.dialogs.HelperAlertDialog
import com.summer.passwordmanager.ui.screens.main.viewmodels.TagViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class TagFrag : BaseFragment<FragMainTagBinding>() {

    override val layoutResId: Int
        get() = R.layout.frag_main_tag

    private var tagListAdapter: TagListAdapter? = null

    private val viewModel: TagViewModel by activityViewModel()

    private val optionsFrag by lazy {
        OptionsFrag(dismissListener = object :
            OptionsFrag.DismissListener<TagEntity> {
            override fun onDelete(model: TagEntity) {
                (activity as BaseActivity<*>).run {
                    initHelperDialog(dialogType = HelperAlertDialog.DialogType.TWO_BUTTON)
                    helperDialog?.run {
                        setTitleText(this@TagFrag.getString(R.string.delete))
                        setContentText(this@TagFrag.getString(R.string.are_you_sure_you_want_to_delete))
                        setCancelClickListener {
                            dismiss()
                        }
                        setConfirmClickListener {
                            dismiss()
                            viewModel.deleteTagById(model.id)
                        }
                    }
                }
            }

            override fun onEdit(model: TagEntity) {
                showCreateTagDialog(model)
            }
        })
    }

    private var createTagDialog: CreateTagDialog? = null

    override fun onFragmentReady(instanceState: Bundle?) {
        initRecyclerView()
        observeViewModel()
        listeners()
    }

    private fun observeViewModel() {
        viewModel.tagsLive.observe(viewLifecycleOwner) {
            mBinding?.pgFragTagTags?.isVisible = it == null
            tagListAdapter?.submitList(it ?: listOf())
        }
    }

    private fun initRecyclerView() {
        mBinding?.run {
            (rvFragTagTags.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            tagListAdapter = TagListAdapter(object : TagListAdapter.SelectionCallBack {
                override fun onItemLongPress(item: TagEntity) {
                    optionsFrag.setModel(model = item)
                    optionsFrag.show(childFragmentManager, null)
                }
            })
            rvFragTagTags.adapter = tagListAdapter
        }
    }

    private fun showCreateTagDialog(tagEntity: TagEntity?) {
        if (createTagDialog?.isAdded != true) {
            createTagDialog =
                CreateTagDialog(dismissListener = object : CreateTagDialog.DismissLister {
                    override fun onSave(tagEntity: TagEntity) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            viewModel.insertTagEntity(tagEntity)
                        }
                    }
                }, tagEntity = tagEntity)
            createTagDialog?.show(childFragmentManager, "")
        }
    }

    private fun listeners() {
        mBinding?.run {
            fabFragMainTagAdd.setOnClickListener {
                showCreateTagDialog(null)
            }
        }
    }
}