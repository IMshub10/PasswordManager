package com.summer.passwordmanager.ui.screens.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseActivity
import com.summer.passwordmanager.databinding.ActivityMainBinding
import com.summer.passwordmanager.ui.screens.main.viewmodels.VaultViewModel
import com.summer.passwordmanager.utils.extensions.closeApp
import com.summer.passwordmanager.utils.extensions.gone
import com.summer.passwordmanager.utils.extensions.visible
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResId: Int
        get() = R.layout.activity_main

    private val navController: NavController by lazy { findNavController(R.id.fcv_main_container) }

    private val mainViewModel: VaultViewModel by viewModel()

    private var tagItem: MenuItem? = null
    private var searchItem: MenuItem? = null

    override fun onActivityReady(savedInstanceState: Bundle?) {
        mBinding?.tbActSectorProfileToolbar?.let {
            setupActionBar(it)
        }
        setupActionBarWithNavController(
            navController, AppBarConfiguration(
                setOf(
                    R.id.vaultFrag,
                    R.id.passGeneratorFrag,
                    R.id.profileFrag,
                )
            )
        )
        mBinding?.bmvMainNavigation?.setupWithNavController(navController)
        setUpFragmentNavigation()
    }

    private fun setUpFragmentNavigation() {
        navController.addOnDestinationChangedListener { _, destination, arguments ->
            mBinding?.tbActSectorProfileToolbar?.title = destination.label
            setMenuItemsVisibility(navController.currentDestination)
            when (destination.id) {
                R.id.vaultFrag -> {
                    mBinding?.bmvMainNavigation?.visible()
                    mBinding?.tbActSectorProfileToolbar?.navigationIcon = null
                }

                R.id.passGeneratorFrag -> {
                    (arguments?.getBoolean("fetchPass") ?: false).let {
                        mBinding?.bmvMainNavigation?.isVisible = !it
                        mBinding?.tbActSectorProfileToolbar?.navigationIcon =
                            if (it) ResourcesCompat.getDrawable(
                                resources, R.drawable.ic_navigation_back, null
                            ) else null
                    }
                }

                R.id.profileFrag -> {
                    mBinding?.bmvMainNavigation?.visible()
                    mBinding?.tbActSectorProfileToolbar?.navigationIcon = null
                }

                R.id.tagFrag, R.id.createVaultFrag, R.id.fileExportDetailsFrag, R.id.fileImportDetailsFrag -> {
                    mBinding?.bmvMainNavigation?.gone()
                    mBinding?.tbActSectorProfileToolbar?.navigationIcon =
                        ResourcesCompat.getDrawable(resources, R.drawable.ic_navigation_back, null)
                }
            }
        }
        mBinding?.tbActSectorProfileToolbar?.setNavigationOnClickListener {
            navController.navigateUp()
        }
        mBinding?.bmvMainNavigation?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_vault -> {
                    navController.navigate(R.id.vaultFrag)
                }

                R.id.item_generator -> {
                    navController.navigate(R.id.passGeneratorFrag)
                }

                R.id.item_profile -> {
                    navController.navigate(R.id.profileFrag)
                }

                else -> {
                    throw RuntimeException("Unknown item Id")
                }
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        tagItem = menu?.findItem(R.id.item_tag)
        searchItem = menu?.findItem(R.id.item_search)

        setMenuItemsVisibility(navController.currentDestination)
        val searchView = menu?.findItem(R.id.item_search)?.actionView as SearchView?
        searchView?.maxWidth = Int.MAX_VALUE
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                mainViewModel.searchQuery.value = query ?: ""
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mainViewModel.searchQuery.value = newText ?: ""
                return false
            }
        })
        searchView?.setOnSearchClickListener {
            mBinding?.tbActSectorProfileToolbar?.title = null
            mBinding?.bmvMainNavigation?.gone()
        }
        searchView?.setOnCloseListener {
            mBinding?.tbActSectorProfileToolbar?.title = navController.currentDestination?.label
            mBinding?.bmvMainNavigation?.visible()
            return@setOnCloseListener false
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() {
        when (navController.currentDestination?.id) {
            in arrayOf(
                R.id.createVaultFrag,
                R.id.fileImportDetailsFrag,
                R.id.fileExportDetailsFrag,
                R.id.tagFrag
            ) -> {
                navController.popBackStack()
            }

            R.id.passGeneratorFrag -> {
                val fetchPass =
                    navController.currentBackStackEntry?.arguments?.getBoolean("fetchPass") ?: false
                if (fetchPass) {
                    navController.popBackStack()
                } else {
                    closeApp()
                }
            }

            else -> {
                closeApp()
            }
        }
    }

    private fun setMenuItemsVisibility(currentDestination: NavDestination?) {
        if (currentDestination?.id == R.id.vaultFrag) {
            searchItem?.isVisible = true
            tagItem?.isVisible = true
        } else {
            searchItem?.isVisible = false
            tagItem?.isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.item_tag) {
            if (navController.currentDestination?.id == R.id.vaultFrag) {
                navController.navigate(R.id.action_vaultFrag_to_tagFrag)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}