package com.higgs.githubusers.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.higgs.githubusers.DetailActivity
import com.higgs.githubusers.R
import com.higgs.githubusers.data.model.UserList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserAdapter(private val listUsers: ArrayList<UserList>, private val context :Context, private val viewModel: UserViewModel) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    companion object {
        private const val TAG = "UserAdapter"
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonDetail: LinearLayout = view.findViewById<LinearLayout>(R.id.button_detail)
        val imageViewSiteAdmin: ImageView = view.findViewById<ImageView>(R.id.image_view_site_admin)
        val textViewLogin: TextView = view.findViewById<TextView>(R.id.text_view_login)
        val imageViewAvatarUrl: ImageView = view.findViewById<ImageView>(R.id.image_view_avatar_url)
        val imageViewFavorite: ImageView = view.findViewById<ImageView>(R.id.image_view_favorite)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.adapter_users_list, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        Glide.with(viewHolder.imageViewAvatarUrl).load(listUsers[position].avatar_url)
            .transition(DrawableTransitionOptions.withCrossFade()).centerCrop()
            .into(viewHolder.imageViewAvatarUrl)
        viewHolder.textViewLogin.text = listUsers[position].login
        viewHolder.imageViewSiteAdmin.visibility =
            if (listUsers[position].site_admin) View.VISIBLE else View.INVISIBLE

        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(listUsers[position].id)
            withContext(Dispatchers.Main){
                if(count != null){
                    if(count > 0)
                        viewHolder.imageViewFavorite.setImageResource(R.drawable.ic_baseline_heart_red_24)
                    else
                        viewHolder.imageViewFavorite.setImageResource(R.drawable.ic_baseline_heart_gray_24)
                }
            }
        }

        viewHolder.buttonDetail.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putString(DetailActivity.EXTRA_USERNAME, listUsers[position].login)
            bundle.putInt(DetailActivity.EXTRA_id, listUsers[position].id)
            val intent: Intent = Intent(context, DetailActivity::class.java).apply {
                putExtras(bundle)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = listUsers.size

}