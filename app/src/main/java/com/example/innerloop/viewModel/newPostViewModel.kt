package com.example.innerloop.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.innerloop.models.PostModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class newPostViewModel: ViewModel() {
    private val db = FirebaseDatabase.getInstance()
    val postRef = db.getReference("posts")
    val storageRef = Firebase.storage.reference
    val imageRef = storageRef.child("posts/${UUID.randomUUID()}.jpg")

    private val _isPosted = MutableLiveData<Boolean>()
    val isPosted:LiveData<Boolean> = _isPosted

     fun saveImageNData(postContent: String, uid:String,imageUri: Uri) {
        val upload = imageRef.putFile(imageUri)
        upload.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(postContent,uid,it.toString())
            }
        }
    }

     fun saveData(postContent: String, uid:String,imageUrl: String){
        val postData = PostModel(postContent,uid, imageUrl,System.currentTimeMillis().toString())
        postRef.child(postRef.push().key!!).setValue(postData)
            .addOnSuccessListener {
                _isPosted.postValue(true)
            }.addOnFailureListener{
                _isPosted.postValue(false)
            }
    }


}