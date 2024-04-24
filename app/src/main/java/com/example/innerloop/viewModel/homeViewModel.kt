package com.example.innerloop.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.innerloop.models.PostModel
import com.example.innerloop.models.UserModel
import com.example.innerloop.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class homeViewModel: ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val postRef = db.getReference("posts")

    private var _postsNUser = MutableLiveData<List<Pair<PostModel,UserModel>>>()
    val postsNUser:LiveData<List<Pair<PostModel,UserModel>>> = _postsNUser

    init {
        fetchPostsNUsers {
            _postsNUser.value = it
        }
    }

    private fun fetchPostsNUsers(onResult: (List<Pair<PostModel,UserModel>>) -> Unit){
        postRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val result = mutableListOf<Pair<PostModel,UserModel>>()
                for(postSnapshot in snapshot.children){
                    val post = postSnapshot.getValue(PostModel::class.java)
                    post.let {
                        fetchUserFromPost(it!!){ user->
                            result.add(0,it to user)

                            if(result.size==snapshot.childrenCount.toInt()){
                                onResult(result)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetchUserFromPost(postModel:PostModel,onResult:(UserModel)->Unit){
        db.getReference("users").child(postModel.userId).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                user?.let(onResult)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}