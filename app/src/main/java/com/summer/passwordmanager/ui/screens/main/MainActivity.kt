package com.summer.passwordmanager.ui.screens.main

import android.content.Intent
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseActivity
import com.summer.passwordmanager.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResId: Int
        get() = R.layout.activity_main

    private val navController: NavController by lazy { findNavController(R.id.fcv_main_container) }

    override fun onActivityReady(savedInstanceState: Bundle?) {
        setupActionBar(mBinding.tbActSectorProfileToolbar)
        setupActionBarWithNavController(
            navController, AppBarConfiguration(
                setOf(
                    R.id.vaultFrag,
                    R.id.passGeneratorFrag,
                    R.id.settingsFrag,
                )
            )
        )
        mBinding.bmvMainNavigation.setupWithNavController(navController)
        setUpFragmentNavigation()
    }

    private fun setUpFragmentNavigation() {
        mBinding.bmvMainNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_vault -> {
                    navController.navigate(R.id.vaultFrag)
                }
                R.id.item_generator -> {
                    navController.navigate(R.id.passGeneratorFrag)
                }
                R.id.item_settings -> {
                    navController.navigate(R.id.settingsFrag)
                }
                else -> {
                    throw RuntimeException("Unknown item Id")
                }
            }
            true
        }
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        startActivity(a)
    }
}