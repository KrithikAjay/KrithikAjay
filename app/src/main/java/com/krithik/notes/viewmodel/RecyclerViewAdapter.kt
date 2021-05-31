package com.krithik.notes.viewmodel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.krithik.notes.database.User
import com.krithik.notes.databinding.ListItemBinding

class RecyclerViewAdapter(private val clickListener: (User) -> Unit) : ListAdapter<User,RecyclerViewAdapter.ViewHolder>(DiffUtilCallback) {
    private val usersList = ArrayList<User>()
    companion object DiffUtilCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.name == newItem.name
        }
    }


    class ViewHolder(private val binding:  ListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(user: User, clickListener: (User) -> Unit) {
            binding.nameTextView.text = user.name
            binding.emailTextView.text = user.email
            binding.listItemLayout.setOnClickListener {
                clickListener(user)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerViewAdapter.ViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder((ListItemBinding.inflate((layoutInflater))))
    }
    fun setList(users: List<User>) {
        usersList.clear()
        usersList.addAll(users)

    }

    override fun onBindViewHolder(holder: RecyclerViewAdapter.ViewHolder, position: Int) {

        holder.bind(getItem(position), clickListener)

    }
}


