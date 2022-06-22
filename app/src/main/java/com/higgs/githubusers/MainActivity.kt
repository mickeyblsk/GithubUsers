package com.higgs.githubusers

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.higgs.githubusers.ui.MainViewModel
import com.higgs.githubusers.ui.UserAdapter
import com.higgs.githubusers.ui.UserViewModel


class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerViewUsersResult: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerViewUsersResult = findViewById<RecyclerView>(R.id.recycler_view_users_result)
        recyclerViewUsersResult.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()
        searchUser()
    }

    fun searchUserButtonOnclick(view: View) {
        searchUser()
    }

    private fun searchUser(){
        val searchStr: String = findViewById<EditText>(R.id.edit_text_search_text).text.toString()
        viewModel.setSearchUserList(searchStr)
        viewModel.getSearchUserList().observe(this) {
            if (it != null) {
                val userAdapter: UserAdapter = UserAdapter(
                    it, this, ViewModelProvider(this).get(
                        UserViewModel::class.java
                    )
                )
                recyclerViewUsersResult.adapter = userAdapter
                recyclerViewUsersResult.invalidate()
            }
        }
    }

}