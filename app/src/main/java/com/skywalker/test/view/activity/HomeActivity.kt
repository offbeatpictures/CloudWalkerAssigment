package com.skywalker.test.view.activity

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.skywalker.test.view.adpaters.ViewPagerAdpater
import com.skywalker.test.view.fragment.Task_One
import com.skywalker.test.view.fragment.Task_Three
import com.skywalker.test.view.fragment.Task_Two


class HomeActivity : AppCompatActivity() {


    lateinit var viewPager2: ViewPager2;
    lateinit var tabLayout: TabLayout;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.skywalker.test.R.layout.activity_main)
        initViews();
        initPagerAdpater();
        initTabLayoutMediator()


    }

    private fun initTabLayoutMediator() {
        TabLayoutMediator(
            tabLayout, viewPager2
        ) { tab: TabLayout.Tab, position: Int ->
            when (position) {
                0 -> tab.text = "TASK ONE"
                1 -> tab.text = "TASK TWO"
                2 -> tab.text = "TASK THREE"
            }
        }.attach()
    }

    private fun initPagerAdpater() {
        val listOfFragment = arrayListOf(
            Task_One(),
            Task_Two(),
            Task_Three()
        )
        val viewPagerAdpater = ViewPagerAdpater(this@HomeActivity, listOfFragment)
        viewPager2.apply {
            adapter = viewPagerAdpater
        }
    }

    private fun initViews() {
        tabLayout = findViewById(com.skywalker.test.R.id.tab_layout)
        viewPager2 = findViewById(com.skywalker.test.R.id.view_pager2)
    }


}