package com.devdroid.express

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.devdroid.express.databinding.ActivityLogInBinding
import com.devdroid.express.databinding.ActivityLogInBinding.inflate
import com.devdroid.express.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseDatabase
    lateinit var Users : ArrayList<User>
    lateinit var mAdapter: MainListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db= FirebaseDatabase.getInstance()

        if(auth.currentUser==null){
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }
        Users = ArrayList()
        mAdapter= MainListAdapter(this,Users)

        binding.mainrecyclerView.adapter = mAdapter



        val dbRef = db.getReference("users/")
        dbRef.addValueEventListener(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                Users.clear()
                for(postSnapshot in snapshot.children){
                    val currentUser = postSnapshot.getValue(User::class.java)
                    if(auth.currentUser?.uid!=currentUser?.uid) {
                        Users.add(currentUser!!)
                    }
                }
                mAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==R.id.signOut){
            auth.signOut()
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater : MenuInflater = menuInflater
        inflater.inflate(R.menu.signout,menu)
        return super.onCreateOptionsMenu(menu)
    }
}