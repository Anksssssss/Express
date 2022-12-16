package com.devdroid.express

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.devdroid.express.MainActivity
import com.devdroid.express.R
import com.devdroid.express.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {

    lateinit var binding : ActivitySignInBinding
    lateinit var auth : FirebaseAuth
    lateinit var db :FirebaseDatabase

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
            startActivity(Intent(this,LogInActivity::class.java))
        }


    }

    private fun signIn(){
        val email = binding.edtSignInEmail.text.toString()
        val password = binding.edtSignInPassword.text.toString()
        val name = binding.edtSignInUsername.text.toString()

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task->
            if(task.isSuccessful){
                val uid = auth.currentUser?.uid.toString()
                val user = User(name,uid)
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