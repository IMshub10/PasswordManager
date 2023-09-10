package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagViewModel(private val repository: Repository) : ViewModel() {

    val tagsLive: LiveData<List<TagEntity>?>
        get() = repository.getAllTagsLive()

    fun deleteTagById(tagId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTagById(tagId)
        }
    }

    suspend fun insertTagEntity(tagEntity: TagEntity) =
        repository.insertReplaceTagEntity(tagEntity)

}