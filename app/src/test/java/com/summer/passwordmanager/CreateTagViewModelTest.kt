package com.summer.passwordmanager

import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.ui.dialogs.CreateTagViewModel
import com.summer.passwordmanager.utils.AppUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CreateTagViewModelTest {

    private lateinit var viewModel: CreateTagViewModel

    @Before
    fun setup() {
        viewModel = CreateTagViewModel()
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `test toSaveTagEntity() when tagEntity is not null`() {
        viewModel.tagEntity = TagEntity()
        viewModel.tagNameEditTextModel.editTextContent = "TestName"
        viewModel.tagDescriptionEditTextModel.editTextContent = "TestDescription"

        val result = viewModel.toSaveTagEntity()

        assertEquals("TestName", result.name)
        assertEquals("TestDescription", result.description)
    }

    @Test
    fun `test toSaveTagEntity() when tagEntity is null`() {
        viewModel.tagEntity = null

        val result = viewModel.toSaveTagEntity()

        assertEquals("", result.name)
        assertNull(result.description)
    }

    @Test
    fun `test setUpInputModels() when tagEntity is not null`() {
        viewModel.tagEntity = TagEntity(name = "ExistingName", description = "ExistingDescription")

        viewModel.setUpInputModels()

        assertEquals("ExistingName", viewModel.tagNameEditTextModel.editTextContent)
        assertEquals("ExistingDescription", viewModel.tagDescriptionEditTextModel.editTextContent)
    }

    @Test
    fun `test setUpInputModels() when tagEntity is null`() {
        viewModel.tagEntity = null

        viewModel.setUpInputModels()

        assertEquals("", viewModel.tagNameEditTextModel.editTextContent)
        assertEquals("", viewModel.tagDescriptionEditTextModel.editTextContent)
    }

    @Test
    fun `test toSaveTagEntity() when tagEntity is not null and fields are empty`() {
        viewModel.tagEntity = TagEntity()

        viewModel.tagNameEditTextModel.editTextContent = ""
        viewModel.tagDescriptionEditTextModel.editTextContent = ""

        val result = viewModel.toSaveTagEntity()

        assertEquals("", result.name)
        assertEquals(result.description, result.description)
        assertEquals(result.updatedAtApp, AppUtils.getCurrentTimeSecs())
    }

    @Test
    fun `test toSaveTagEntity() when tagEntity is not null and tagDescriptionEditTextModel is null`() {
        viewModel.tagEntity = TagEntity()

        viewModel.tagNameEditTextModel.editTextContent = "TestName"
        viewModel.tagDescriptionEditTextModel.editTextContent = null

        val result = viewModel.toSaveTagEntity()

        assertEquals("TestName", result.name)
        assertNull(result.description)
        assertEquals(result.updatedAtApp, AppUtils.getCurrentTimeSecs())
    }

    @Test
    fun `test validate() for tagNameEditTextModel when content is too short`() {
        viewModel.tagNameEditTextModel.editTextContent = "A" // Less than the required length

        val isValid = viewModel.tagNameEditTextModel.validate()

        assertFalse(isValid)
    }
}
