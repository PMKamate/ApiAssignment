package com.demo.testassignment

import android.graphics.Color
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.demo.testassignment.ui.main.BookApiFragment
import com.demo.testassignment.ui.main.NewsApiFragment
import com.demo.testassignment.ui.main.SectionsPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        sectionsPagerAdapter.addFragment(NewsApiFragment(), "News")
        sectionsPagerAdapter.addFragment(BookApiFragment(), "Book")
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setTabTextColors(Color.GRAY,Color.WHITE); // set the tab text colors for the both states of the tab.

        tabs.setupWithViewPager(viewPager)

    }
}