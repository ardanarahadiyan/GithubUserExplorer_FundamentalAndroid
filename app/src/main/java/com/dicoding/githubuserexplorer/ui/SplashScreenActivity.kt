package com.dicoding.githubuserexplorer.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.dicoding.githubuserexplorer.R
import com.dicoding.githubuserexplorer.ui.main.MainActivity
import com.dicoding.githubuserexplorer.ui.setting.SettingPreferences
import com.dicoding.githubuserexplorer.ui.setting.SettingViewModel
import com.dicoding.githubuserexplorer.ui.setting.SettingViewModelFactory


private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("setting")

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this,
            SettingViewModelFactory(pref)
        )[SettingViewModel::class.java]

        settingViewModel.getThemeSetting().observe(this){isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        Handler().postDelayed({
            val splashIntent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(splashIntent)
            finish()
        }, 2000)



    }
}