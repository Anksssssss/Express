package com.devdroid.express

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class ChatAdapter(val context: Context , val messageList : ArrayList<Messages>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val ITEM_SENT = 1
    val ITEM_RECIEVED = 2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        if(viewType == ITEM_SENT){
            val view:View = LayoutInflater.from(context).inflate(R.layout.from,parent , false)
            return FromViewHolder(view)
        }else{
            val view:View = LayoutInflater.from(context).inflate(R.layout.to,parent , false)
            return ToViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if(holder.javaClass == FromViewHolder::class.java){
            val viewHolder = holder as FromViewHolder
            holder.from.text = currentMessage.message

        }else{
            val viewHolder = holder as ToViewHolder
            holder.to.text = currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECIEVED
        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class FromViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val from = itemView.findViewById<TextView>(R.id.txtFrom)
    }
    class ToViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val to = itemView.findViewById<TextView>(R.id.txtTo)
    }

}