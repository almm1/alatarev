package com.example.gif

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MainFragment(0)
            1 -> MainFragment(1)
            2 -> MainFragment(2)
            else -> return MainFragment(3)
        }
    }
    override fun getCount(): Int {
        return 4
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Последние"
            1 -> "Лучшие"
            2 -> "Горячие"
            else -> return "Случайные"
        }
    }
}
