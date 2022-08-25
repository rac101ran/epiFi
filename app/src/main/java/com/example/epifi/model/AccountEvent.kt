package com.example.epifi.model

sealed  class AccountEvent {
    data class PanChanged(val pan : String) : AccountEvent()

    data class DobD(val dobD: String) : AccountEvent()
    data class DobM(val dobM : String) : AccountEvent()
    data class DobY(val dobY : String) : AccountEvent()

    object Submit : AccountEvent()
}