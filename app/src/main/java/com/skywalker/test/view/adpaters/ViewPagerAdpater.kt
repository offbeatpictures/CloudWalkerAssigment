package com.skywalker.test.view.adpaters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdpater(fa: FragmentActivity, private val fragments:ArrayList<Fragment>) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int =fragments.size;

    override fun createFragment(position: Int): Fragment = fragments.get(position)
}