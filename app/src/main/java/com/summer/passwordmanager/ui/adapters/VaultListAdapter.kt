package com.summer.passwordmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.summer.passwordmanager.R
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.databinding.ItemListVaultBinding

class VaultListAdapter(private val selectionCallBack: SelectionCallBack) :
    ListAdapter<VaultEntity, VaultListAdapter.ContentHolder>(Callback()) {

    inner class ContentHolder(private val binding: ItemListVaultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: VaultEntity) {
            binding.run {
                this.model = model

                clItemListVaultRoot.setOnClickListener {
                    binding.tvItemListVaultTag.isSelected = false
                    binding.tvItemListVaultTag.isSelected = true
                    selectionCallBack.onItemClick(item = model)
                }
                clItemListVaultRoot.setOnLongClickListener {
                    selectionCallBack.onLongPress(item = model)
                    return@setOnLongClickListener true
                }
                ivItemListVaultViewPass.setOnClickListener {
                    selectionCallBack.onPassVisibilityClick(item = model)
                }
                ivItemListVaultCopyPass.setOnClickListener {
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
        ) = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: VaultEntity,
            newItem: VaultEntity,
        ) = oldItem.id == newItem.id

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContentHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_list_vault,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ContentHolder, position: Int) =
        holder.bind(getItem(position))

}