package com.crobridge.points

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.crobridge.points.ui.main.SectionsPagerAdapter
import com.crobridge.points.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.crobridge.points.db.PointDb
import com.crobridge.points.db.Polyline
import com.crobridge.points.ui.main.PageViewModel
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PointViewModel
    private lateinit var polylines : List<Polyline>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myToolbar)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            when (position){
                0 -> tab.text = getString(R.string.tab_text_1)
                1 -> tab.text = getString(R.string.tab_text_2)
            }
        }.attach()
        val fab: FloatingActionButton = binding.fab

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val dataSource = PointDb.getInstance(application).pointDbDao
        val viewModelFactory = ViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PointViewModel::class.java)
        viewModel.polylines.observe(this, Observer {
            polylines = it
        } )

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.overflow_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_game -> {
                newGame()
                true
            }
            R.id.load_game -> {
                loadGame()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadGame() {
        val fmt = SimpleDateFormat.getDateInstance()
        // val fmt = SimpleDateFormat.getDateTimeInstance()
        // val fmt = SimpleDateFormat("dd.MM.yyyy hh:mm")
        val items = polylines.map {
            "${it.name}, ${fmt.format(it.startTimeMilliSec)}"
        }.toTypedArray()
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Games")
        builder.setItems(items) { dialog, index ->
            // curent_game_id = index
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun newGame(){
        val dataSource = PointDb.getInstance(application).pointDbDao
        val viewModelFactory = ViewModelFactory(dataSource, application)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(PointViewModel::class.java)
        val p = Polyline()
        p.name = "two"
        viewModel.addPolyline(p)
    }
}