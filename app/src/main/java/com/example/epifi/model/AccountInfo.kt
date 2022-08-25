package com.example.epifi.model


data class AccountInfo (
    var PAN : String = "" , var errorPan : String? = null , var errorDob : String? = null ,
    var dobD : String = "", var dobM : String = "" , var dobY : String = ""
)