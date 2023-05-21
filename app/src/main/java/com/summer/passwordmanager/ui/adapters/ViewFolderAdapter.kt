package com.summer.passwordmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.summer.passwordmanager.R
import com.summer.passwordmanager.database.entities.FolderEntity
import com.summer.passwordmanager.databinding.ItemViewFolderBinding

class ViewFolderAdapter (private val selectionCallBack: SelectionCallBack) :
    ListAdapter<FolderEntity, ViewFolderAdapter.ContentHolder>(Callback()) {

    inner class ContentHolder(private val binding: ItemViewFolderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: FolderEntity) {
            binding.run {
                this.model = model
                tvItemViewFolderRoot.setOnClickListener {
                    selectionCallBack.onItemClick(item = model)
                }
            }
        }
    }

    interface SelectionCallBack {
        fun onItemClick(item: FolderEntity)
    }

    internal class Callback : DiffUtil.ItemCallback<FolderEntity>() {
        override fun areItemsTheSame(
            oldItem: FolderEntity,
            newItem: FolderEntity,
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: FolderEntity,
            newItem: FolderEntity,
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContentHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_view_folder,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ContentHolder, position: Int) {
        holder.bind(getItem(position))
    }
}