package com.devdroid.express

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.devdroid.express.MainActivity
import com.devdroid.express.R
import com.devdroid.express.databinding.ActivityLogInBinding
import com.devdroid.express.databinding.ActivitySignInBinding
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    lateinit var binding : ActivityLogInBinding
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogInBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            Login()
        }


    }

    private fun Login(){
        val email = binding.edtLoginEmail.text.toString()
        val password = binding.edtloginPassword.text.toString()

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {task->
            if(task.isSuccessful){
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }else{
                Toast.makeText(this,"Authentication Failed",Toast.LENGTH_SHORT).show()
            }
        }
    }
}