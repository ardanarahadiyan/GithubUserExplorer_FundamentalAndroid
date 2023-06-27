package com.dicoding.githubuserexplorer.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuserexplorer.helper.Event
import com.dicoding.githubuserexplorer.data.remote.ApiConfig
import com.dicoding.githubuserexplorer.data.remote.GithubResponse
import com.dicoding.githubuserexplorer.data.remote.ItemsItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    companion object{
        const val TAG = "MainViewModel"
        const val USERNAME = "ardana"
    }

    private val _listUserSearch = MutableLiveData<List<ItemsItem>>()
    val listUserSearch : LiveData<List<ItemsItem>> = _listUserSearch

    private val _totalFound = MutableLiveData<Int>()
    val totalFound : LiveData<Int> = _totalFound

    private val _listUserFollowing = MutableLiveData<List<ItemsItem>>()
    val listUserFollowing : LiveData<List<ItemsItem>> = _listUserFollowing

    private val _listUserFollowers = MutableLiveData<List<ItemsItem>>()
    val listUserFollowers : LiveData<List<ItemsItem>> = _listUserFollowers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading :LiveData<Boolean> = _isLoading

    private val _snackbarError = MutableLiveData<Event<String>>()
    val snackbarError : LiveData<Event<String>> = _snackbarError

    init {
        findUserSearch(USERNAME)
    }

    fun findUserSearch(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchUser(username)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUserSearch.value = response.body()?.items
                        _totalFound.value = response.body()?.totalCount
                    }
                }else{
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _snackbarError.value = Event("onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _snackbarError.value = Event("onFailure: ${t.message}")
            }

        })
    }

    fun findUserFollow (key : Int, username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService()
        val adapt  = when (key){
            1 -> {client.getFollowing(username)}
            else -> {client.getFollowers(username)}
        }
        adapt.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        when (key) {
                            1 -> _listUserFollowing.value = response.body()
                            else -> _listUserFollowers.value = response.body()
                        }
                    }
                }else{
                    Log.e(TAG, "onFollowFailure: ${response.message()}")
                    _snackbarError.value = Event("onFollowFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFollowFailure: ${t.message}")
                _snackbarError.value = Event("onFollowFailure: ${t.message}")
            }

        })
    }

}