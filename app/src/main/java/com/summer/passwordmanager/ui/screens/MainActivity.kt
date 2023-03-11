package com.summer.passwordmanager.ui.screens

import android.os.Bundle
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseActivity
import com.summer.passwordmanager.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResId: Int
        get() = R.layout.activity_main

    override fun onActivityReady(savedInstanceState: Bundle?) {
        setupActionBar(mBinding.tbActSectorProfileToolbar)
        /*initHelperDialog()
        helperDialog?.run {
            setTitleText("Testing")
            setCancelClickListener {
                dismiss()
                helperDialog = null
            }
        }*/
    }

}