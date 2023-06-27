package com.dicoding.githubuserexplorer.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.dicoding.githubuserexplorer.ui.favorite.FavoriteActivity
import com.dicoding.githubuserexplorer.ui.main.MainActivity
import com.dicoding.githubuserexplorer.R
import com.dicoding.githubuserexplorer.ui.setting.SettingActivity
import com.dicoding.githubuserexplorer.ui.adapter.SectionPageAdapter
import com.dicoding.githubuserexplorer.data.local.FavoriteUsers
import com.dicoding.githubuserexplorer.data.remote.DetailUserResponse
import com.dicoding.githubuserexplorer.databinding.ActivityDetailUserBinding
import com.dicoding.githubuserexplorer.helper.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var detailBinding: ActivityDetailUserBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }

    companion object{

        private const val TAG = "DetailUserActivity"
        private lateinit var USERNAME : String
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)
        supportActionBar?.title = "Detail User"
        supportActionBar?.elevation = 0f
        detailBinding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)



        val dataUsername = intent.getStringExtra("username")
        if (dataUsername != null) {
            USERNAME = dataUsername
        }

        detailViewModel.isLoading.observe(this){showLoading(it)}
        detailViewModel.findUserDetail(dataUsername.toString())
        detailViewModel.dataUserDetail.observe(this){detailUser->
            setDetailData(detailUser)
        }
        detailViewModel.snackbarError.observe(this){
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(window.decorView.rootView,snackBarText, Snackbar.LENGTH_SHORT).setTextMaxLines(5).show()
            }
        }

        val sectionPageAdapter = SectionPageAdapter(this)
        sectionPageAdapter.username = dataUsername.toString()
        val viewPager : ViewPager2 = detailBinding.viewPager
        viewPager.adapter = sectionPageAdapter
        val tabs : TabLayout = detailBinding.tabs
        TabLayoutMediator(tabs, viewPager){tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        var isFavorite = false
        detailViewModel.getFavoriteUserByUsername(dataUsername.toString()).observe(this) {
            if (it != null) {
                detailBinding.fabAdd.setImageResource(R.drawable.ic_fav)
                isFavorite = !isFavorite
            } else {
                detailBinding.fabAdd.setImageResource(R.drawable.ic_fav_outline)
            }

        }

        detailBinding.fabAdd.setOnClickListener {
            val user = FavoriteUsers(
                detailViewModel.dataUserDetail.value?.login.toString(),
                detailViewModel.dataUserDetail.value?.avatarUrl,
                detailViewModel.dataUserDetail.value?.htmlUrl)
            if (isFavorite){
                detailViewModel.delete(user)
                isFavorite = !isFavorite
            } else  {
                detailViewModel.insert(user)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.detail_to_home_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.homeMenu ->{
                val homeIntent = Intent(this@DetailUserActivity,
                MainActivity::class.java)
                homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(homeIntent)
            }
            R.id.shareUser ->{
                val sharedText = "Github User :\n\n${detailViewModel.dataUserDetail.value?.login}\n${detailViewModel.dataUserDetail.value?.htmlUrl}"
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,sharedText)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent,null)
                startActivity(shareIntent)
            }
            R.id.favoriteDetailMenu ->{
                val favoriteIntent = Intent(this@DetailUserActivity, FavoriteActivity::class.java)
                startActivity(favoriteIntent)
            }
            R.id.settingDetailMenu ->{
                val settingIntent = Intent(this@DetailUserActivity, SettingActivity::class.java)
                startActivity(settingIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDetailData(detailUser: DetailUserResponse){
        detailBinding.tvDisplayName.text = detailUser.name
        detailBinding.tvDetailUsername.text = detailUser.login
        detailBinding.tvBio.text = detailUser.bio.toString()
        detailBinding.detailList.tvCompany.text = detailUser.company
        detailBinding.detailList.tvLocation.text = detailUser.location
        detailBinding.detailList.tvEmail.text = detailUser.email.toString()
        detailBinding.detailList.tvBlog.text = detailUser.blog
        detailBinding.tvFollowing.text = "${detailUser.following} Mengikuti"
        detailBinding.tvFollowers.text = "${detailUser.followers} Pengikut"
        detailBinding.tvRepos.text = "${detailUser.publicRepos} Repositori"
        Glide.with(detailBinding.ivDetailImage)
            .load(detailUser.avatarUrl)
            .into(detailBinding.ivDetailImage)

        if (detailUser.bio == null){
            detailBinding.tvBio.visibility = View.GONE
        }
        if (detailUser.company == null){
            detailBinding.detailList.tvCompany.visibility = View.GONE
            detailBinding.detailList.ivCompany.visibility = View.GONE
        }
        if (detailUser.location == null){
            detailBinding.detailList.ivLocation.visibility = View.GONE
            detailBinding.detailList.tvLocation.visibility = View.GONE
        }
        if (detailUser.email == null){
            detailBinding.detailList.tvEmail.visibility = View.GONE
            detailBinding.detailList.ivEmail.visibility = View.GONE
        }
        if (detailUser.blog == ""){
            detailBinding.detailList.ivBlog.visibility = View.GONE
            detailBinding.detailList.tvBlog.visibility = View.GONE
        }

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            detailBinding.progressBarDetail.visibility = View.VISIBLE
        } else {
            detailBinding.progressBarDetail.visibility = View.GONE
        }
    }

}