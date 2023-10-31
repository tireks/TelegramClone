package com.tirex_projs.telegram_clone.models

data class User(
    val id: String = "",
    var username: String = "",
    var bio: String = "",
    var fullname: String = "",
    var state: String = "",
    var phone:String = "",
    var photoURL : String = "empty"
)