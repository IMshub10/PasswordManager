package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.repository.Repository
import com.summer.passwordmanager.utils.AppUtils
import com.summer.passwordmanager.utils.extensions.filterByTagSearchValue

class VaultViewModel(private val repository: Repository) : ViewModel() {

    private val selectedTag = MutableLiveData<TagEntity?>(null)

    val searchQuery = MutableLiveData("")

    val tagListLive = repository.getAllTagsLive().map {
        val list = it.toMutableList()
        list.add(0, TagEntity(
            id = AppUtils.KEY_ALL, createdAtApp = 0,
            updatedAtApp = 0, name = "All", description = null
        ).apply {
            isSelected = true
        })
        return@map list
    }

    private fun getAllVaultsWithTheirTag(): LiveData<Map<VaultEntity, TagEntity?>> {
        return repository.getAllVaultsWithTheirTag()
    }

    fun resetSelectedTag(tagEntity: TagEntity) {
        selectedTag.value = if (tagEntity.id == AppUtils.KEY_ALL) null else tagEntity
        if (tagListLive.value == null) return
        tagListLive.value?.forEach {
            it.isSelected = it.id == tagEntity.id
            it.notifyChange()
        }
    }

    fun getFilteredVaultList() =
        GetFilteredVaultList(searchQuery, selectedTag, getAllVaultsWithTheirTag())

    inner class GetFilteredVaultList(
        searchQuery: MutableLiveData<String>,
        selectedTag: MutableLiveData<TagEntity?>,
        vaultMap: LiveData<Map<VaultEntity, TagEntity?>>
    ) : MediatorLiveData<List<VaultEntity>?>() {
        init {
            addSource(vaultMap) {
                value = if (it == null) null
                else {
                    getVaultList(it, searchQuery.value ?: "", selectedTag.value)
                }
            }
            addSource(searchQuery) {
                val vaultMapValue = vaultMap.value
                value = if (vaultMapValue == null) null else {
                    getVaultList(vaultMapValue, it ?: "", selectedTag.value)
                }
            }
            addSource(selectedTag) {
                val vaultMapValue = vaultMap.value
                value = if (vaultMapValue == null) null
                else {
                    getVaultList(vaultMapValue, searchQuery.value ?: "", it)
                }
            }
        }

        private fun getVaultList(
            vaultMap: Map<VaultEntity, TagEntity?>,
            searchValue: String,
            selectedTag: TagEntity?
        ): List<VaultEntity> {
            return vaultMap.toList().map { pair ->
                pair.first.apply {
                    tagEntity = pair.second
                }
            }.filter { filterModel ->
                filterModel.filterByTagSearchValue(selectedTag?.id, searchValue)
            }
        }
    }

}