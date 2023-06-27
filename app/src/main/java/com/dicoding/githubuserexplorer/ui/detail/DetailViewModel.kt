package com.dicoding.githubuserexplorer.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserexplorer.data.remote.ApiConfig
import com.dicoding.githubuserexplorer.data.remote.DetailUserResponse
import com.dicoding.githubuserexplorer.helper.Event
import com.dicoding.githubuserexplorer.ui.main.MainViewModel
import com.dicoding.githubuserexplorer.data.local.FavoriteUsers
import com.dicoding.githubuserexplorer.repository.FavoriteUsersRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel (application: Application) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _dataUserDetail = MutableLiveData<DetailUserResponse>()
    val dataUserDetail : LiveData<DetailUserResponse> = _dataUserDetail

    private val _snackbarError = MutableLiveData<Event<String>>()
    val snackbarError : LiveData<Event<String>> = _snackbarError

    private val mFavoriteUsersRepository : FavoriteUsersRepository = FavoriteUsersRepository(application)

    fun insert (users: FavoriteUsers) {
        mFavoriteUsersRepository.insert(users)
    }

    fun update(users: FavoriteUsers){
        mFavoriteUsersRepository.update(users)
    }

    fun delete (users: FavoriteUsers){
        mFavoriteUsersRepository.delete(users)
    }

    fun getFavoriteUserByUsername(username: String) :LiveData<FavoriteUsers> = mFavoriteUsersRepository.getFavoriteUserByUsername(username)

    fun findUserDetail(userName: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(userName)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (responseBody != null){
                    _dataUserDetail.value = response.body()
                } else {
                    Log.e(MainViewModel.TAG, "onDetailFailure: ${response.message()}")
                    _snackbarError.value = Event("onDetailFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(MainViewModel.TAG, "onDetailFailure: ${t.message}")
                _snackbarError.value = Event("onDetailFailure: ${t.message}")
            }
        })

    }



}