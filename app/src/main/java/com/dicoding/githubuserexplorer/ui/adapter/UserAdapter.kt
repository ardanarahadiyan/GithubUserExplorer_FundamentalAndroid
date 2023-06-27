package com.dicoding.githubuserexplorer.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuserexplorer.R
import com.dicoding.githubuserexplorer.data.remote.ItemsItem
import com.dicoding.githubuserexplorer.ui.detail.DetailUserActivity

class UserAdapter(private val listUser : List<ItemsItem>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder (view : View) :RecyclerView.ViewHolder(view){
        fun bind (githubResponse: ItemsItem){
            tvItem.text = githubResponse.login
            Glide.with(ivProfPic)
                .load(githubResponse.avatarUrl)
                .into(ivProfPic)
            tvUrl.text = githubResponse.htmlUrl
        }
        private val tvItem : TextView = view.findViewById(R.id.tvItemUsername)
        private val ivProfPic : ImageView = view.findViewById(R.id.ivProfilePic)
        private val tvUrl : TextView = view.findViewById(R.id.tvUrlUsername)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view,parent,false)
        return UserViewHolder(view)
    }

    override fun getItemCount() = listUser.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(listUser[position])
        holder.itemView.setOnClickListener {
            val detailIntent = Intent(holder.itemView.context, DetailUserActivity::class.java)
            detailIntent.putExtra("username",listUser[position].login)
            holder.itemView.context.startActivity(detailIntent)
        }
    }
}