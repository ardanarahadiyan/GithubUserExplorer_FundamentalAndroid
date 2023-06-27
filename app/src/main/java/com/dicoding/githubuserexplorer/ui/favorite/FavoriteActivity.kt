package com.dicoding.githubuserexplorer.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuserexplorer.ui.main.MainActivity
import com.dicoding.githubuserexplorer.R
import com.dicoding.githubuserexplorer.ui.setting.SettingActivity
import com.dicoding.githubuserexplorer.ui.adapter.UserAdapter
import com.dicoding.githubuserexplorer.data.local.FavoriteUsers
import com.dicoding.githubuserexplorer.data.remote.ItemsItem
import com.dicoding.githubuserexplorer.databinding.ActivityFavoriteBinding
import com.dicoding.githubuserexplorer.helper.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteBinding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel> {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        favoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(favoriteBinding.root)

        favoriteViewModel.getFavoriteUsers().observe(this){ user : List<FavoriteUsers> ->
            val favUser = arrayListOf<ItemsItem>()
            user.map {
                val users = ItemsItem(login = it.username,
                    avatarUrl = it.avatarUrl,
                    htmlUrl = it.githubUrl)
                favUser.add(users)
            }
            favoriteBinding.rvFavorite.adapter = UserAdapter(favUser)
            favoriteBinding.rvFavorite.setHasFixedSize(true)
            favoriteBinding.rvFavorite.layoutManager = LinearLayoutManager(this)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.favorite_to_home_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.homeMenu ->{
                val homeIntent = Intent(this@FavoriteActivity,
                    MainActivity::class.java)
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(homeIntent)
            }
            R.id.settingFavMenu->{
                val settingIntent = Intent(this@FavoriteActivity,
                SettingActivity::class.java)
                startActivity(settingIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}