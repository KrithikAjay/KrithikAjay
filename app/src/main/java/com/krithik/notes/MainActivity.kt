package com.krithik.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.krithik.notes.database.User
import com.krithik.notes.database.UserDatabase
import com.krithik.notes.database.UserRepository
import com.krithik.notes.databinding.ActivityMainBinding
import com.krithik.notes.generated.callback.OnClickListener
import com.krithik.notes.viewmodel.RecyclerViewAdapter
import com.krithik.notes.viewmodel.UserViewModel
import com.krithik.notes.viewmodel.UserViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var adapter : RecyclerViewAdapter
    private lateinit var userViewModel : UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        val dao = UserDatabase.getInstance(application).userDao
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this,factory).get(UserViewModel::class.java)
        binding.userViewModel = userViewModel
        binding.lifecycleOwner = this


        userViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
        initRecyclerView()
        userViewModel.userList.observe(this, Observer {
            it.let {
                adapter.submitList(it)
            }
        })

    }


    private fun initRecyclerView(){
        binding.userRecyclerView.layoutManager = LinearLayoutManager(this)
        adapter = RecyclerViewAdapter { selectedItem: User -> listItemClicked(selectedItem) }
        binding.userRecyclerView.adapter = adapter

    }
    private fun listItemClicked(user : User){
        userViewModel.initUpdateAndDelete(user)    }
}


