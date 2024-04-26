package com.example.innerloop.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.innerloop.models.UserModel
import com.example.innerloop.utils.SharedPref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class authViewModel: ViewModel() {
    val auth = FirebaseAuth.getInstance()

    //Store user info in db
    private val db = FirebaseDatabase.getInstance()
    val userRef = db.getReference("users")
    //To save the post image
    val storageRef = Firebase.storage.reference
    val imageRef = storageRef.child("users/${UUID.randomUUID()}.jpg")

    private val _firebaseUser = MutableLiveData<FirebaseUser?>()
    val firebaseUser:LiveData<FirebaseUser?> = _firebaseUser

    //For error message
    private val _error = MutableLiveData<String>()
    val error:LiveData<String> = _error

    //Initializing as the view model initializes
    init{
        _firebaseUser.value = auth.currentUser
    }

    fun registerNewUser(email:String, password:String, name:String, userName:String,imageUri: Uri, bio:String, context:Context){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    _firebaseUser.postValue(auth.currentUser)
                    savePfp(email,password,name,userName, imageUri, bio, auth.currentUser?.uid, context)
                }
                else{
                    _error.postValue("Something went wrong")
                }
            }
    }

    private fun savePfp(email: String, password: String, name: String, userName: String, imageUri:Uri, bio: String, uid: String?, context:Context) {
        val upload = imageRef.putFile(imageUri)
        upload.addOnSuccessListener {
            imageRef.downloadUrl.addOnSuccessListener {
                saveData(email,password,name,userName, it.toString(), bio, uid,context)
            }
        }
    }

    private fun saveData(email: String, password: String, name: String, userName: String, downloadedUrl: String, bio: String, uid: String?, context:Context){
        val userData = UserModel(email,password,name,userName,downloadedUrl,bio,uid!!)
        userRef.child(uid).setValue(userData)
            .addOnSuccessListener {
                SharedPref.storeUserData(email,name,userName,downloadedUrl,bio, context)
            }.addOnFailureListener{}

        val firestoreDb = Firebase.firestore
        val followersRef = firestoreDb.collection("followers").document(uid)
        val followingRef = firestoreDb.collection("following").document(uid)

        followersRef.set(mapOf("followerIds" to listOf<String>()))
        followingRef.set(mapOf("followingIds" to listOf<String>()))
    }

    fun login(email: String, password: String, context: Context){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    _firebaseUser.postValue(auth.currentUser)
                    getData(auth.currentUser!!.uid, context)
                }
                else{
                    _error.postValue(it.exception!!.message)
                }
            }
    }

    private fun getData(uid: String, context: Context) {
        userRef.child(uid).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val userData = snapshot.getValue(UserModel::class.java)
                if (userData != null) {
                    SharedPref.storeUserData(userData.email,userData.name,userData.userName,userData.downloadedUrl,userData.bio, context)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun logout(){
        auth.signOut()
        _firebaseUser.postValue(null)
    }
}