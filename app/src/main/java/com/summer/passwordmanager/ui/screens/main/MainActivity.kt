package com.summer.passwordmanager.ui.screens.main

import android.content.Intent
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.NavArgument
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseActivity
import com.summer.passwordmanager.databinding.ActivityMainBinding
import com.summer.passwordmanager.utils.gone
import com.summer.passwordmanager.utils.visible

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
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            mBinding.tvActSectorProfileTitle.text = destination.label
            when (destination.id) {
                R.id.createVaultFrag -> {
                    mBinding.bmvMainNavigation.gone()
                    mBinding.tbActSectorProfileToolbar.navigationIcon =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_navigation_back, null)
                }

                R.id.passGeneratorFrag -> {
                    (arguments?.getBoolean("fetchPass") ?: false).let {
                        mBinding.bmvMainNavigation.isVisible = !it
                        mBinding.tbActSectorProfileToolbar.navigationIcon = if (it)
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_navigation_back,
                                null
                            ) else null
                    }
                }

                else -> {
                    mBinding.bmvMainNavigation.visible()
                    mBinding.tbActSectorProfileToolbar.navigationIcon = null
                }
            }
        }
        mBinding.tbActSectorProfileToolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
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
        if (navController.currentDestination?.id in arrayOf(
                R.id.createVaultFrag,
                R.id.passGeneratorFrag
            )
        ) {
            navController.popBackStack()
        } else {
            startActivity(Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_HOME)
            })
        }
    }
}