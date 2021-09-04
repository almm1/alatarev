package com.example.gif

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.gif.fragments.HotFragment
import com.example.gif.fragments.LatestFragment
import com.example.gif.fragments.TopFragment

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                TopFragment()
            }
            1 -> HotFragment()
            else -> {
                return LatestFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Лучшие"
            1 -> "Горячие"
            else -> {
                return "Последние"
        }
    }
}
}
