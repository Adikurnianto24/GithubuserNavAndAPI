package com.adiandroid.githubusernavandapi.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adiandroid.githubusernavandapi.databinding.ItemUserBinding
import com.adiandroid.githubusernavandapi.model.User
import com.bumptech.glide.Glide

class ListDataAdapter() : RecyclerView.Adapter<ListDataAdapter.UserViewHolder>() {
    private val list = ArrayList<User>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallBack(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setList(users: List<User>) {
        list.clear()
        list.addAll(users)
        notifyDataSetChanged()
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: User) {
            binding.apply {
                usernameTv.text = user.login

                binding.root.setOnClickListener {
                    onItemClickCallback?.onItemClicked(user)
                }

                Glide.with(binding.userIv.context)
                    .load(user.avatarUrl)
                    .into(binding.userIv)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}