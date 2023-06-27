package com.dicoding.githubuserexplorer.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserexplorer.R
import com.dicoding.githubuserexplorer.data.remote.ItemsItem
import com.dicoding.githubuserexplorer.databinding.ActivityMainBinding
import com.dicoding.githubuserexplorer.ui.adapter.UserAdapter
import com.dicoding.githubuserexplorer.ui.favorite.FavoriteActivity
import com.dicoding.githubuserexplorer.ui.setting.SettingActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    companion object{
        private var USERNAME = "initial_username"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "GitHub Users Explorer"


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvSearch.setHasFixedSize(true)
        binding.rvSearch.layoutManager = LinearLayoutManager(this)

        val mainViewModel = ViewModelProvider(this,
            ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]

        mainViewModel.isLoading.observe(this){showLoading(it)}
        mainViewModel.snackbarError.observe(this){
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(window.decorView.rootView,snackBarText,Snackbar.LENGTH_SHORT).setTextMaxLines(5).show()
            }
        }
        mainViewModel.listUserSearch.observe(this){ listUserSearch ->
            setUserSearch(listUserSearch)
        }
        mainViewModel.totalFound.observe(this){totalFound ->
            totalUserFound(totalFound)
        }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = binding.search
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                USERNAME = query
                mainViewModel.findUserSearch(USERNAME)
                mainViewModel.listUserSearch.observe(this@MainActivity){ listUserSearch ->
                    setUserSearch(listUserSearch)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.home_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.favoriteMenu -> {
                val favoriteIntent = Intent(this@MainActivity,
                    FavoriteActivity::class.java)
                startActivity(favoriteIntent)
            }
            R.id.settingMenu ->{
                val settingIntent = Intent(this@MainActivity,
                    SettingActivity::class.java)
                startActivity(settingIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }



    private fun setUserSearch (listUserSearch : List<ItemsItem>){
        val userSearchAdapter  = UserAdapter(listUserSearch)
        binding.rvSearch.adapter = userSearchAdapter
    }

    private fun totalUserFound (response : Int){
        binding.totalFound.text = "Total user yang ditemukan : $response"
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }


}
