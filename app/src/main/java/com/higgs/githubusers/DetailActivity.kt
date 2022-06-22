package com.higgs.githubusers

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.higgs.githubusers.ui.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "DetailActivity"
        const val EXTRA_USERNAME = "extra_user"
        const val EXTRA_id = "extra_id"
    }

    private lateinit var username: String
    private var userId: Int = 0

    private lateinit var viewModel: UserViewModel
    private lateinit var recyclerViewFollowers: RecyclerView
    private lateinit var recyclerViewFollowing: RecyclerView
    private lateinit var toggleButtonFavorite: ToggleButton
    private var _isChecked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val bundle: Bundle = intent.extras!!
        username = bundle.getString(EXTRA_USERNAME)!!
        userId = bundle.getInt(EXTRA_id)!!

        recyclerViewFollowers = findViewById<RecyclerView>(R.id.recycler_view_followers)
        recyclerViewFollowing = findViewById<RecyclerView>(R.id.recycler_view_following)
        toggleButtonFavorite = findViewById<ToggleButton>(R.id.toggle_button_favorite)
        recyclerViewFollowers.layoutManager = LinearLayoutManager(this)
        recyclerViewFollowing.layoutManager = LinearLayoutManager(this)

        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        viewModel.setUserDetail(username)
        viewModel.getUserDetail().observe(this) {
            if (it != null) {
                Glide.with(findViewById<ImageView>(R.id.image_view_avatar_url)).load(it.avatar_url)
                    .transition(
                        DrawableTransitionOptions.withCrossFade()
                    ).centerCrop().into(findViewById<ImageView>(R.id.image_view_avatar_url))

                findViewById<TextView>(R.id.text_view_name).text =
                    "${getString(R.string.name)}:\n ${it.name}"
                findViewById<TextView>(R.id.text_view_login).text =
                    "${getString(R.string.login)}:\n" + " ${it.login}"
                findViewById<TextView>(R.id.text_view_bio).text =
                    "${getString(R.string.bio)}:\n ${it.bio}"
                findViewById<TextView>(R.id.text_view_site_admin).text =
                    "${getString(R.string.site_admin)}:\n ${if (it.site_admin) " Yes " else " No "}"
                findViewById<TextView>(R.id.text_view_location).text =
                    "${getString(R.string.location)}:\n ${it.location}"
                findViewById<TextView>(R.id.text_view_blog).text =
                    "${getString(R.string.blog)}:\n ${it.blog}"
                findViewById<TextView>(R.id.text_view_followers).text =
                    "${getString(R.string.followers)}(${it.followers})"
                findViewById<TextView>(R.id.text_view_following).text =
                    "${getString(R.string.following)}(${it.following})"
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(userId)
            withContext(Dispatchers.Main){
                if(count != null){
                    _isChecked = count > 0
                    toggleButtonFavorite.isChecked = _isChecked
                }
            }
        }

        setFollows(username)
    }

    fun closePage(view: View) {
        finish()
    }

    private fun setFollows(username: String) {
        val followersViewModel: FollowersViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)

        val followingViewModel: FollowingViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)

        followersViewModel.setFollowers(username)
        followingViewModel.setFollowing(username)

        followersViewModel.getFollowers().observe(this) {
            if (it != null) {
                Log.i("Tag", "FollowersViewModel: ${it.toString()}")
                val followsUserAdapter: FollowsUserAdapter = FollowsUserAdapter(it, this)
                recyclerViewFollowers.adapter = followsUserAdapter
                recyclerViewFollowers.invalidate()
            }
        }

        followingViewModel.getFollowing().observe(this) {
            if (it != null) {
                Log.i("Tag", "FollowingViewModel: ${it.toString()}")
                val followsUserAdapter: FollowsUserAdapter = FollowsUserAdapter(it, this)
                recyclerViewFollowing.adapter = followsUserAdapter
                recyclerViewFollowing.invalidate()
            }
        }
    }

    fun favoriteButtonClick(view: View) {
        _isChecked = !_isChecked
        if(_isChecked)
            viewModel.addToFavorite(userId, username)
        else
            viewModel.removeFromFavorite(userId)
        toggleButtonFavorite.isChecked = _isChecked
    }
}