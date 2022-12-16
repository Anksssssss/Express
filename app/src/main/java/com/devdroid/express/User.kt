package com.devdroid.express

class User (val name:String,val uid:String,val imageUrl :String="") {
    constructor():this("","","")
}