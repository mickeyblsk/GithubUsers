package com.higgs.githubusers.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.higgs.githubusers.api.RetrofitClient
import com.higgs.githubusers.data.model.UserList
import com.higgs.githubusers.data.model.UserListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainViewModel"
    }
    val userList: MutableLiveData<ArrayList<UserList>> = MutableLiveData<ArrayList<UserList>>()

    fun setSearchUserList(query: String) {
        RetrofitClient.usersListApiInstance
            .getSearchUsers(query)
            .enqueue(object : Callback<UserListResponse> {
                override fun onResponse(
                    call: Call<UserListResponse>,
                    response: Response<UserListResponse>
                ) {
                    if(response.isSuccessful){
                        userList.postValue(response.body()?.items)
                    }
                }

                override fun onFailure(call: Call<UserListResponse>, t: Throwable) {
                    Log.d("${TAG}Failure", t.message.toString())
                }

            })
    }

    fun getSearchUserList(): LiveData<ArrayList<UserList>> = userList
}