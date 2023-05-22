package com.summer.passwordmanager.ui.screens.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.summer.passwordmanager.database.entities.FolderEntity
import com.summer.passwordmanager.database.entities.VaultEntity
import com.summer.passwordmanager.repository.Repository

class VaultViewModel(private val repository: Repository) : ViewModel() {

    fun getAllVaultsWithTheirFolder(): LiveData<Map<VaultEntity, FolderEntity?>>{
        return repository.getAllVaultsWithTheirFolder()
    }
}