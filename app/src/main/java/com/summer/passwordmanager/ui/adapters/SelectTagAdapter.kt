package com.summer.passwordmanager.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.summer.passwordmanager.R
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.databinding.ItemSelectTagBinding

class SelectTagAdapter(private val selectionCallBack: SelectionCallBack) :
    ListAdapter<TagEntity, SelectTagAdapter.ContentHolder>(Callback()) {

    inner class ContentHolder(private val binding: ItemSelectTagBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(model: TagEntity) {
            binding.run {
                this.model = model
                tvItemSelectTagTitle.setOnClickListener {
                    selectionCallBack.onItemClick(item = model)
                }
            }
        }
    }

    interface SelectionCallBack {
        fun onItemClick(item: TagEntity)
    }

    internal class Callback : DiffUtil.ItemCallback<TagEntity>() {
        override fun areItemsTheSame(
            oldItem: TagEntity,
            newItem: TagEntity,
        ): Boolean {
            return false
        }

        override fun areContentsTheSame(
            oldItem: TagEntity,
            newItem: TagEntity,
        ): Boolean {
            return false
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContentHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_select_tag,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ContentHolder, position: Int) {
        holder.bind(getItem(position))
    }
}