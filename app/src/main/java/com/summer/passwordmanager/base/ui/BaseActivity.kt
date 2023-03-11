package com.summer.passwordmanager.base.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.summer.passwordmanager.ui.dialogs.HelperAlertDialog


abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

    @get:LayoutRes
    protected abstract val layoutResId: Int

    var helperDialog: HelperAlertDialog? = null

    lateinit var mBinding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        onPreCreated()
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, layoutResId)
        onActivityReady(savedInstanceState)
    }

    protected fun setupActionBar(toolbar: Toolbar) {
        setSupportActionBar(toolbar)
    }

    protected open fun onPreCreated() {}

    protected abstract fun onActivityReady(savedInstanceState: Bundle?)

    fun enableBack() {
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            showExitDialog()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    protected open fun showExitDialog() {

    }

    fun initHelperDialog(dialogType: HelperAlertDialog.DialogType = HelperAlertDialog.DialogType.NO_BUTTON) {
        if (helperDialog == null) {
            helperDialog = HelperAlertDialog(dialogType)
        } else {
            helperDialog?.changeAlertType(dialogType)
        }
        if (helperDialog?.isAdded != true) {
            helperDialog?.show(supportFragmentManager, "")
        }
    }
}