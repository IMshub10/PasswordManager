package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TagViewModel(private val localRepository: LocalRepository) : ViewModel() {

    val tagsLive: LiveData<List<TagEntity>?>
        get() = localRepository.getAllTagsLive()

    fun deleteTagById(tagId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.deleteTagById(tagId)
        }
    }

    suspend fun insertTagEntity(tagEntity: TagEntity) =
        localRepository.insertReplaceTagEntity(tagEntity)

}