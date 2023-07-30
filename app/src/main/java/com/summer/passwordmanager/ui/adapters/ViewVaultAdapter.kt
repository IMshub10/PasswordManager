package com.summer.passwordmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.summer.passwordmanager.R
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.databinding.ItemViewVaultBinding
import com.summer.passwordmanager.utils.extensions.showShortToast

class ViewVaultAdapter(private val selectionCallBack: SelectionCallBack) :
    ListAdapter<VaultEntity, ViewVaultAdapter.ContentHolder>(Callback()) {

    inner class ContentHolder(private val binding: ItemViewVaultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: VaultEntity) {
            binding.run {
                this.model = model

                clItemViewVaultRoot.setOnClickListener {
                    binding.tvItemViewVaultTag.isSelected = false
                    binding.tvItemViewVaultTag.isSelected = true
                    selectionCallBack.onItemClick(item = model)
                }
                clItemViewVaultRoot.setOnLongClickListener {
                    selectionCallBack.onLongPress(item = model)
                    return@setOnLongClickListener true
                }
                ivItemViewVaultViewPass.setOnClickListener {
                    selectionCallBack.onPassVisibilityClick(item = model)
                }
                ivItemViewVaultCopyPass.setOnClickListener {
                    selectionCallBack.onCopyPas(item = model)
                }
            }
        }
    }

    interface SelectionCallBack {
        fun onItemClick(item: VaultEntity)
        fun onLongPress(item: VaultEntity)
        fun onPassVisibilityClick(item: VaultEntity)
        fun onCopyPas(item: VaultEntity)
    }

    internal class Callback : DiffUtil.ItemCallback<VaultEntity>() {
        override fun areItemsTheSame(
            oldItem: VaultEntity,
            newItem: VaultEntity,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: VaultEntity,
            newItem: VaultEntity,
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContentHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_view_vault,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ContentHolder, position: Int) {
        holder.bind(getItem(position))
    }
}