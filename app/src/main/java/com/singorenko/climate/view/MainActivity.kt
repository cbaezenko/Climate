package com.singorenko.climate.view

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayout
import com.singorenko.climate.R
import com.singorenko.climate.adapter.MyPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
//import com.singorenko.climate.databinding.MainCityLayoutBinding

class MainActivity : AppCompatActivity() {

//    private lateinit var binding: MainCityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        initTabLayout()
//        initViewPager()
    }

    override fun onResume() {
        super.onResume()
        initTabLayout()
        initViewPager()

    }

    private fun initTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Home"))
        tabLayout.addTab(tabLayout.newTab().setText("Cities"))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}
            override fun onTabSelected(p0: TabLayout.Tab?) {
                viewPager.currentItem = p0?.position!!
            }
        })
    }

    private fun initViewPager() {
        viewPager.adapter = MyPagerAdapter(supportFragmentManager, tabLayout.tabCount)
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }
}
