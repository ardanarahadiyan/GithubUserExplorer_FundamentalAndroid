package com.dicoding.githubuserexplorer.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.githubuserexplorer.data.local.FavUsersRoomDatabase
import com.dicoding.githubuserexplorer.data.local.FavoriteUsers
import com.dicoding.githubuserexplorer.data.local.FavoriteUsersDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteUsersRepository (application: Application){
    private val mFavoriteUsersDao : FavoriteUsersDao
    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavUsersRoomDatabase.getDatabase(application)
        mFavoriteUsersDao = db.favoriteUsersDao()
    }

    fun getAllUsers() : LiveData<List<FavoriteUsers>> = mFavoriteUsersDao.getAllUsers()
    fun getFavoriteUserByUsername(username: String) : LiveData<FavoriteUsers> = mFavoriteUsersDao.getFavoriteUserByUsername(username)


    fun insert(users: FavoriteUsers) {
        executorService.execute{mFavoriteUsersDao.insert(users)}
    }

    fun delete(users: FavoriteUsers){
        executorService.execute { mFavoriteUsersDao.delete(users)}
    }

    fun update(users: FavoriteUsers){
        executorService.execute{mFavoriteUsersDao.update(users)}
    }

}