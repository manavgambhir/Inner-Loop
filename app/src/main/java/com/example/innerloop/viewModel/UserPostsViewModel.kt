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
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.auth.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class UserPostsViewModel: ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    private val postRef = db.getReference("posts")
    private val userRef = db.getReference("users")

    private var _posts = MutableLiveData<List<PostModel>>()
    val posts:LiveData<List<PostModel>> = _posts

    private var _user = MutableLiveData(UserModel())
    val user:LiveData<UserModel> = _user


    //Follower and Following Lists
    private var _followerList = MutableLiveData<List<String>>()
    val followerList:LiveData<List<String>> = _followerList

    private var _followingList = MutableLiveData<List<String>>()
    val followingList:LiveData<List<String>> = _followingList

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

    val firestoreDb = Firebase.firestore
    fun followUsers(userId: String, currentUserId: String){
        val followingRef = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userId)

        followingRef.update("followingIds", FieldValue.arrayUnion(userId))
        followerRef.update("followerIds", FieldValue.arrayUnion(currentUserId))
    }

    fun unfollowUser(userId: String, currentUserId: String){
        val followingRef = firestoreDb.collection("following").document(currentUserId)
        val followerRef = firestoreDb.collection("followers").document(userId)

        followingRef.update("followingIds",FieldValue.arrayRemove(userId))
        followerRef.update("followerIds", FieldValue.arrayRemove(currentUserId))
    }

    fun getFollowers(userId: String){
        firestoreDb.collection("followers").document(userId)
            .addSnapshotListener { value, error ->
                val followerIds = value?.get("followerIds") as? List<String> ?: listOf()
                _followerList.postValue(followerIds)
        }
    }

    fun getFollowing(userId: String){
        firestoreDb.collection("following").document(userId)
            .addSnapshotListener { value, error ->
                val followingIds = value?.get("followingIds") as? List<String>?: listOf()
                _followingList.postValue(followingIds)
            }
    }


}