package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.utils.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class VaultViewModel(private val repository: Repository) : ViewModel() {

    val tagListLive = MutableLiveData<List<TagEntity>>(null)

    val selectedTag = MutableLiveData<TagEntity>(null)

    fun getAllVaultsWithTheirTag(): LiveData<Map<VaultEntity, TagEntity?>> {
        return repository.getAllVaultsWithTheirTag()
    }

    fun getAllTags() {
        viewModelScope.launch(Dispatchers.Default) {
            val allList = repository.getAllTags().toMutableList()
            allList.add(0, TagEntity(AppUtils.KEY_ADD, "All", "", 0, 0).apply {
                isSelected = true
            })
            tagListLive.postValue(allList)
        }
    }

    fun resetSelectedTag(tagEntity: TagEntity) {
        selectedTag.value = tagEntity
        if (tagListLive.value == null) return
        tagListLive.value?.forEach {
            it.isSelected = it.id == tagEntity.id
            it.notifyChange()
        }
    }
}