package com.stylingandroid.oreo.notifications

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem

class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        supportActionBar?.apply {
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.takeIf { it.itemId == android.R.id.home }?.run {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
