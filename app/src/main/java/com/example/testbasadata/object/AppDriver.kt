package com.example.testbasadata.`object`

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.testbasadata.R
import com.example.testbasadata.fragments.FragmentSettings
import com.example.testbasadata.utilits.USER
import com.example.testbasadata.utilits.downloadAndSetImage
import com.example.testbasadata.utilits.replaceFragment
import com.mikepenz.iconics.Iconics.applicationContext
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader
import com.mikepenz.materialdrawer.util.DrawerImageLoader

class AppDriver(private val activityTelegram: AppCompatActivity, private val toolbar: Toolbar) {

    private lateinit var mDrawer: Drawer
    private lateinit var mHeader: AccountHeader
    private lateinit var mDrawerLayout: DrawerLayout
    private lateinit var mCurrentProfile: ProfileDrawerItem


    fun create() {
        initLoader()
        createHeader()
        createDrawer()
        mDrawerLayout = mDrawer.drawerLayout
    }

    fun disableDrawer() {
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = false
        activityTelegram.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        toolbar.setNavigationOnClickListener {
            activityTelegram.supportFragmentManager.popBackStack()
        }


    }

    fun enableDrawer() {
        activityTelegram.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        mDrawer.actionBarDrawerToggle?.isDrawerIndicatorEnabled = true
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        toolbar.setNavigationOnClickListener {
            mDrawer.openDrawer()

        }
    }

    private fun createDrawer() {
        mDrawer = DrawerBuilder()
            .withActivity(activityTelegram)
            .withToolbar(toolbar)
            .withActionBarDrawerToggle(true)
            .withSelectedItem(-1)
            .withAccountHeader(mHeader)
            .addDrawerItems(
                PrimaryDrawerItem().withIdentifier(100)
                    .withIconTintingEnabled(true)
                    .withName("Cтворити групу")
                    .withSelectable(false)
                    .withIcon(R.drawable.friends)
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                        ): Boolean {
                            Toast.makeText(
                                applicationContext,
                                position.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            return false

                        }

                    }),
                PrimaryDrawerItem().withIdentifier(101)
                    .withIconTintingEnabled(true)
                    .withName("Cтворити секретний чат")
                    .withSelectable(false)
                    .withIcon(R.drawable.lock_new)
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                        ): Boolean {
                            Toast.makeText(
                                applicationContext,
                                position.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            return false

                        }

                    }),
                PrimaryDrawerItem().withIdentifier(102)
                    .withIconTintingEnabled(true)
                    .withName("Cтворити канал")
                    .withSelectable(false)
                    .withIcon(R.drawable.demostration)
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                        ): Boolean {
                            Toast.makeText(
                                applicationContext,
                                position.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            return false

                        }

                    }),
                PrimaryDrawerItem().withIdentifier(103)
                    .withIconTintingEnabled(true)
                    .withName("Контакти")
                    .withSelectable(false)
                    .withIcon(R.drawable.person_chat)
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                        ): Boolean {
                            Toast.makeText(
                                applicationContext,
                                position.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            return false

                        }

                    }),
                PrimaryDrawerItem().withIdentifier(104)
                    .withIconTintingEnabled(true)
                    .withName("Дзвінки")
                    .withSelectable(false)
                    .withIcon(R.drawable.telephone)
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                        ): Boolean {
                            Toast.makeText(
                                applicationContext,
                                position.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            return false

                        }

                    }),
                PrimaryDrawerItem().withIdentifier(105)
                    .withIconTintingEnabled(true)
                    .withName("Збережене")
                    .withSelectable(false)
                    .withIcon(R.drawable.bookmark)
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                        ): Boolean {
                            Toast.makeText(
                                applicationContext,
                                position.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            return false

                        }

                    }),
                PrimaryDrawerItem().withIdentifier(106)
                    .withIconTintingEnabled(true)
                    .withName("Налаштування")
                    .withSelectable(false)
                    .withIcon(R.drawable.cogwheel)
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                        ): Boolean {
                            when (position) {
                                7 ->
                                    activityTelegram.replaceFragment(FragmentSettings())
                            }
                            return false
                        }
                    }),
                DividerDrawerItem(),
                PrimaryDrawerItem().withIdentifier(107)
                    .withIconTintingEnabled(true)
                    .withName("Запросити друзів")
                    .withSelectable(false)
                    .withIcon(R.drawable.add_user)
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                        ): Boolean {
                            Toast.makeText(
                                applicationContext,
                                position.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            return false

                        }

                    }),

                PrimaryDrawerItem().withIdentifier(108)
                    .withIconTintingEnabled(true)
                    .withName("Питання про телеграм")
                    .withSelectable(false)
                    .withIcon(R.drawable.information)
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                        ): Boolean {
                            Toast.makeText(
                                applicationContext,
                                position.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            return false

                        }
                    })
            ).build()

    }

    private fun createHeader() {
        mCurrentProfile = ProfileDrawerItem()
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
            .withIdentifier(200)
        mHeader = AccountHeaderBuilder()
            .withActivity(activityTelegram)
            .withHeaderBackground(R.drawable.header)
            .addProfiles(
                mCurrentProfile
                    .withOnDrawerItemClickListener(object : Drawer.OnDrawerItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int,
                            drawerItem: IDrawerItem<*>
                        ): Boolean {
                            Toast.makeText(
                                applicationContext,
                                position.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            return false

                        }

                    }),
            ).build()
    }

    fun updateHeader() {
        mCurrentProfile
            .withName(USER.fullname)
            .withEmail(USER.phone)
            .withIcon(USER.photoUrl)
        mHeader.updateProfile(mCurrentProfile)
    }

    private fun initLoader() {
        DrawerImageLoader.init(object : AbstractDrawerImageLoader() {
            override fun set(imageView: ImageView, uri: Uri, placeholder: Drawable) {
                imageView.downloadAndSetImage(uri.toString())
            }
        })
    }
}