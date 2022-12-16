package com.devdroid.express

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devdroid.express.databinding.ActivityChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatBinding

    lateinit var chatRecyclerView : RecyclerView
    lateinit var messageBox : EditText
    lateinit var sendButton : Button
    lateinit var messageAdapter : ChatAdapter
    lateinit var messageList : ArrayList<Messages>
    lateinit var mDbRef : DatabaseReference

    var senderRoom : String? = null
    var recieverRoom : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)

        val name = intent.getStringExtra("name")
        val recieverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        mDbRef = FirebaseDatabase.getInstance().getReference()

        senderRoom = recieverUid + senderUid
        recieverRoom =  senderUid + recieverUid


        supportActionBar?.title = name

        setContentView(binding.root)

        chatRecyclerView = binding.chatRecyclerView
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        messageBox = binding.edtMessage
        sendButton = binding.btnSendMessage

        messageList = ArrayList()
        messageAdapter = ChatAdapter(this , messageList)
        chatRecyclerView.adapter = messageAdapter

        mDbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Messages::class.java)
                        messageList.add(message!!)
                    }

                    messageAdapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        sendButton.setOnClickListener {

            val message = messageBox.text.toString()
            val messageObject = Messages(message,senderUid.toString())

            mDbRef.child("chats").child(senderRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    mDbRef.child("chats").child(recieverRoom!!).child("messages").push()
                        .setValue(messageObject)
                }

            messageBox.setText("")

        }

    }



}