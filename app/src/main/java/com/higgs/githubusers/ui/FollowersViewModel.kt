package com.higgs.githubusers.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.higgs.githubusers.api.RetrofitClient
import com.higgs.githubusers.data.model.UserList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel: ViewModel() {
    companion object {
        private const val TAG = "FollowersViewModel"
    }
    val listFollowers: MutableLiveData<ArrayList<UserList>> = MutableLiveData<ArrayList<UserList>>()

    fun setFollowers(username: String){
        RetrofitClient.usersListApiInstance
            .getFollowers(username)
            .enqueue(object : Callback<ArrayList<UserList>> {
                override fun onResponse(
                    call: Call<ArrayList<UserList>>,
                    response: Response<ArrayList<UserList>>
                ) {
                    if(response.isSuccessful){
                        listFollowers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserList>>, t: Throwable) {
                    Log.d("${TAG}Failure", t.message.toString())
                }

            })
    }

    fun getFollowers(): LiveData<ArrayList<UserList>> = listFollowers
}