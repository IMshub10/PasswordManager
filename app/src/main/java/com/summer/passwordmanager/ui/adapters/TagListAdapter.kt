package com.summer.passwordmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.summer.passwordmanager.R
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.databinding.ItemListTagBinding

class TagListAdapter(private val selectionCallBack: SelectionCallBack) :
    ListAdapter<TagEntity, TagListAdapter.ContentHolder>(Callback()) {

    inner class ContentHolder(private val binding: ItemListTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TagEntity) {
            binding.run {
                this.model = model
                clItemListTagRoot.setOnLongClickListener {
                    selectionCallBack.onItemLongPress(item = model)
                    return@setOnLongClickListener true
                }
            }
        }
    }

    interface SelectionCallBack {
        fun onItemLongPress(item: TagEntity)
    }

    internal class Callback : DiffUtil.ItemCallback<TagEntity>() {
        override fun areItemsTheSame(
            oldItem: TagEntity,
            newItem: TagEntity,
        ): Boolean = false

        override fun areContentsTheSame(
            oldItem: TagEntity,
            newItem: TagEntity,
        ): Boolean =
            oldItem.id == newItem.id && oldItem.name == newItem.name && oldItem.description == newItem.description
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContentHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_list_tag,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ContentHolder, position: Int) =
        holder.bind(getItem(position))

}