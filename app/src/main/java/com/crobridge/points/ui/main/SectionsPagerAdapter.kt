package com.crobridge.points.ui.main

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(fm: AppCompatActivity) :
    FragmentStateAdapter(fm) {

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> DrawFragment.newInstance(position+1)
            else -> GridFragment.newInstance(position+1)
        }
    }

    override fun getItemCount(): Int {
        return 2
    }
}