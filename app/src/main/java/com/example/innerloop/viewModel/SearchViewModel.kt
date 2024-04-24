package com.example.innerloop.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.innerloop.models.PostModel
import com.example.innerloop.models.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class SearchViewModel: ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")

    private var _allUsers = MutableLiveData<List<UserModel>>()
    val allUsers: LiveData<List<UserModel>> = _allUsers

    init {
         fetchUsers {
             _allUsers.value = it
         }
    }

    private fun fetchUsers(onResult: (List<UserModel>) -> Unit) {
        userRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val res = mutableListOf<UserModel>()
                for(userSnapshot in snapshot.children){
                    val user = userSnapshot.getValue(UserModel::class.java)
                    res.add(user!!)
                }
                onResult(res)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}