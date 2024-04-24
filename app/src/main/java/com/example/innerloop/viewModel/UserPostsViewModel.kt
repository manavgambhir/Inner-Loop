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
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class UserPostsViewModel: ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val postRef = db.getReference("posts")
    val userRef = db.getReference("users")

    private var _posts = MutableLiveData<List<PostModel>>()
    val posts:LiveData<List<PostModel>> = _posts

    private var _user = MutableLiveData(UserModel())
    val user:LiveData<UserModel> = _user

    init {
//        fetchPostsNUsers {
//            _postsNUser.value = it
//        }
    }

    fun fetchUser(uid:String){
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                _user.postValue(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun fetchPosts(uid:String){
        postRef.orderByChild("userId").equalTo(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val postList = snapshot.children.mapNotNull {
                    it.getValue(PostModel::class.java)
                }
                _posts.postValue(postList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}