package com.devdroid.express

import android.content.Intent
import android.net.Uri
import android.os.Bundle

import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.devdroid.express.databinding.ActivitySignInBinding

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.util.*


class SignInActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignInBinding
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseDatabase

    lateinit var uri: Uri
    var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()


        binding.btnSignIn.setOnClickListener {
            signIn()
        }

        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        var getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
            binding.imgSignInUser.setImageURI(it)
            uri=it!!
            if (uri != null) {
                uploadImage(uri)
            }
        }

        binding.imgSignInUser.setOnClickListener {
            getContent.launch("image/*")
        }


    }

    fun uploadImage(uri: Uri) {
        val storageRef = Firebase.storage.reference
        val filename = UUID.randomUUID().toString()
        val ref = storageRef.child("/images/$filename")
        val uploadTask = ref.putFile(uri)


        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
                imageUrl = downloadUri.toString()
            } else {
                Toast.makeText(this,"Failed to upload Image",Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun signIn(){
        val email = binding.edtSignInEmail.text.toString()
        val password = binding.edtSignInPassword.text.toString()
        val name = binding.edtSignInUsername.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                val uid = auth.currentUser?.uid.toString()
                val user = User(name,uid, imageUrl.toString())
                db.getReference().child("users/$uid").setValue(user)
                startActivity(Intent(this,MainActivity::class.java))
                finish()
                Log.d("My Tag",task.toString())
            }else{
                Toast.makeText(this,"Failed to create user",Toast.LENGTH_SHORT).show()
            }
        }

    }

}