package com.demo.testassignment.activity

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.demo.testassignment.R
import com.demo.testassignment.ui.main.BookApiFragment
import com.demo.testassignment.ui.main.NewsApiFragment
import com.demo.testassignment.adapter.TabPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tabPagerAdapter = TabPagerAdapter(supportFragmentManager)
        tabPagerAdapter.addFragment(NewsApiFragment(), "News")
        tabPagerAdapter.addFragment(BookApiFragment(), "Book")
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = tabPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setTabTextColors(Color.GRAY,Color.WHITE); // set the tab text colors for the both states of the tab.

        tabs.setupWithViewPager(viewPager)

    }
}