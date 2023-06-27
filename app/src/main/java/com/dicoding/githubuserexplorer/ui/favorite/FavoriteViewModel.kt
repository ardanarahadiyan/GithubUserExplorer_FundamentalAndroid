package com.dicoding.githubuserexplorer.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserexplorer.data.local.FavoriteUsers
import com.dicoding.githubuserexplorer.repository.FavoriteUsersRepository

class FavoriteViewModel (application: Application) :ViewModel() {

    private val mFavoriteUsersRepository : FavoriteUsersRepository = FavoriteUsersRepository(application)

    fun getFavoriteUsers() : LiveData<List<FavoriteUsers>> = mFavoriteUsersRepository.getAllUsers()
}