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

class FollowingViewModel: ViewModel() {
    companion object {
        private const val TAG = "FollowingViewModel"
    }
    val listFollowing: MutableLiveData<ArrayList<UserList>> = MutableLiveData<ArrayList<UserList>>()

    fun setFollowing(username: String){
        RetrofitClient.usersListApiInstance
            .getFollowing(username)
            .enqueue(object : Callback<ArrayList<UserList>> {
                override fun onResponse(
                    call: Call<ArrayList<UserList>>,
                    response: Response<ArrayList<UserList>>
                ) {
                    if(response.isSuccessful){
                        listFollowing.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserList>>, t: Throwable) {
                    Log.d("${TAG}Failure", t.message.toString())
                }

            })
    }

    fun getFollowing(): LiveData<ArrayList<UserList>> = listFollowing
}