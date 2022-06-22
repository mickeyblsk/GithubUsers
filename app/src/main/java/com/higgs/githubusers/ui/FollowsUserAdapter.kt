package com.higgs.githubusers.ui

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.higgs.githubusers.R
import com.higgs.githubusers.data.model.UserList
import java.lang.Exception

class FollowsUserAdapter(private val listFollowUsers: ArrayList<UserList>, val context :Context) :
    RecyclerView.Adapter<FollowsUserAdapter.ViewHolder>() {
    companion object {
        private const val TAG = "FollowsUserAdapter"
    }

    val handler = Handler(Looper.getMainLooper())

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewLogin: TextView = view.findViewById<TextView>(R.id.text_view_login)
        val imageViewAvatarUrl: ImageView = view.findViewById<ImageView>(R.id.image_view_avatar_url)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_follows_user_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        try {
            viewHolder.textViewLogin.text = listFollowUsers[position].login

            Glide.with(viewHolder.imageViewAvatarUrl).load(listFollowUsers[position].avatar_url)
                .transition(DrawableTransitionOptions.withCrossFade()).centerCrop()
                .into(viewHolder.imageViewAvatarUrl)
        }catch (e: Exception){
            Log.i(TAG, e.message.toString())
        }
    }

    override fun getItemCount() = listFollowUsers.size

}