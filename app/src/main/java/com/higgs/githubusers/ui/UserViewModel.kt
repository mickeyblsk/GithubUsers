package com.higgs.githubusers.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.higgs.githubusers.api.RetrofitClient
import com.higgs.githubusers.data.local.FavoriteUser
import com.higgs.githubusers.data.local.FavoriteUserDao
import com.higgs.githubusers.data.local.UserDatabase
import com.higgs.githubusers.data.model.UserResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "UserViewModel"
    }

    val user: MutableLiveData<UserResponse> = MutableLiveData<UserResponse>()

    private var userDb: UserDatabase? = UserDatabase.getDatabase(application)
    private var userDao: FavoriteUserDao? = userDb?.favoriteUserDao()


    fun setUserDetail(username: String) {
        RetrofitClient.usersListApiInstance
            .getUserDetail(username)
            .enqueue(object : Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: Response<UserResponse>
                ) {
                    if (response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    Log.d("${TAG}Failure", t.message.toString())
                }

            })
    }

    fun getUserDetail(): LiveData<UserResponse> = user

    fun addToFavorite(id: Int, username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteUser(
                id,
                username
            )
            userDao?.addTOFavorite(user)
        }
    }

    fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }
}