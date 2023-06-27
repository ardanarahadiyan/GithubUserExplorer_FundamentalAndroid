package com.dicoding.githubuserexplorer.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteUsersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (users: FavoriteUsers)

    @Update
    fun update(users: FavoriteUsers)

    @Delete
    fun delete(users: FavoriteUsers)

    @Query("SELECT * FROM FavoriteUsers ORDER BY username ASC")
    fun getAllUsers(): LiveData<List<FavoriteUsers>>

    @Query("SELECT * FROM FavoriteUsers WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUsers>
}