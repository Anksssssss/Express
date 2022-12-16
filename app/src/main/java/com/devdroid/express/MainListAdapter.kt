package com.devdroid.express

import android.content.Context
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class MainListAdapter (context:Context,userList:ArrayList<User>): RecyclerView.Adapter<MainListAdapter.ViewHolder>() {

    var user = userList
    val context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.userlist,parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentuser = user[position]
        holder.textView.text = currentuser.name
        if(currentuser.imageUrl!=null) {
            Picasso.get().load(currentuser.imageUrl).into(holder.imageView)
        }else{
            Picasso.get().load(R.drawable.icon).into(holder.imageView)
        }

        holder.itemView.setOnClickListener {
            var intent = Intent(context,ChatActivity::class.java)
            intent.putExtra("name",currentuser.name)
            intent.putExtra("uid",currentuser.uid)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return user.size
    }

    class ViewHolder (itemView : View): RecyclerView.ViewHolder(itemView){
        val textView = itemView.findViewById<TextView>(R.id.txtUserList)
        val imageView = itemView.findViewById<CircleImageView>(R.id.imgUser)
    }

}