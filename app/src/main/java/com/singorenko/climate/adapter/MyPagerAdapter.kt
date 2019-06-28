package com.singorenko.climate.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.singorenko.climate.view.CityListFragment
import com.singorenko.climate.view.MainCityFragment

class MyPagerAdapter(fm: FragmentManager, private var totalTabs: Int) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        return when (position) {
            0 -> {
                return MainCityFragment.newInstance()
            }
            1 -> {
                return CityListFragment.newInstance()
            }
            else -> null
        }
    }

    override fun getCount(): Int {
        return totalTabs
    }
}