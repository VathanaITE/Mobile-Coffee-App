package com.example.coffeeapp.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.coffeeapp.models.UserModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore

class AuthViewModel: ViewModel() {
    private val auth = Firebase.auth
    private val userDb = FirebaseDatabase.getInstance().getReference("users")

    var isLoading by mutableStateOf(false)
  //  private val firestore = Firebase.firestore
    // State to hold the username
  val userName = mutableStateOf("...")

    fun fetchUserData() {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            userDb.child(uid).child("name").get() // Use "name" to match your UserModel
                .addOnSuccessListener { data ->
                    if (data.exists()) {
                        // UPDATE: Set the state so the UI changes!
                        userName.value = data.value.toString()
                    } else {
                        println("No username found ${uid}")
                    }
                }
        }
    }

    fun updatePassword(newPassword: String, onResult: (Boolean, String) -> Unit) {
        val user = auth.currentUser
        val userId = user?.uid
        if(user!=null&&userId!=null){
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Password updated successfully
//                        val database = FirebaseDatabase.getInstance().getReference("users")
                        userDb.child(userId).child("password").setValue(newPassword)
                        onResult(true, "Password updated successfully")
                    } else {
                        onResult(false, task.exception?.localizedMessage ?: "Error updating password")
                    }
                }
        }
    }

    fun editUserName(newName: String, onResult: (Boolean, String) -> Unit) {
        val userId = auth.currentUser?.uid
        if(userId!=null){
            userDb.child(userId).child("name").setValue(newName)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        userName.value = newName
                        // Password updated successfully
                        onResult(true, "Name updated successfully")
                    } else {
                        onResult(false, task.exception?.localizedMessage ?: "Error updating name")
                    }
                }
        }
    }

    fun login(email: String,password: String,onResult: (Boolean, String) -> Unit){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                isLoading = true
                if(it.isSuccessful){
                    onResult(true,"Login successful")
                } else {
                    onResult(false, it.exception?.localizedMessage.toString())
                    isLoading=false
                }
            }
        isLoading = false
    }

    fun signup(name:String,email:String,password:String,onResult:(Boolean, String)->Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                isLoading = true
                if(it.isSuccessful){
                    val userId: String = it.result.user?.uid?: ""
                    val userModel = UserModel(uid = userId,name = name ,email=email, password = password)
                    // CHANGE: Use Realtime Database instead of Firestore
                    val database = FirebaseDatabase.getInstance().getReference("users")
                    database.child(userId).setValue(userModel)
                   // child("userId")
                        .addOnCompleteListener {dbTask->
                            if (dbTask.isSuccessful){
                                onResult(true,"Signup successful")
                            }else{
                                onResult(false,"Failed to save user data")
                            }
                        }
                } else {
                    onResult(false, it.exception?.localizedMessage.toString())
                }
            }
        isLoading = false
    }

}