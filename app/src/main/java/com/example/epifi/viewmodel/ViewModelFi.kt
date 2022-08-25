package com.example.epifi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.epifi.model.AccountEvent
import com.example.epifi.model.AccountInfo
import com.example.epifi.usecases.ValidatePanInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch


class ViewModelFi : ViewModel() {

    var state by mutableStateOf(AccountInfo())

    private val validatePanInfo: ValidatePanInfo = ValidatePanInfo()  // use case class

    // channel can be used for single observer
    private var channel = Channel<ValidationEvent>()

    val validatedFlow = channel.receiveAsFlow()

    fun eventHandler(event: AccountEvent) {
        when (event) {
            is AccountEvent.PanChanged -> {
                state = state.copy(PAN = state.PAN)
            }

            is AccountEvent.DobD -> {
                state = state.copy(dobD = state.dobD)
            }

            is AccountEvent.DobM -> {
                state = state.copy(dobM = state.dobM)
            }

            is AccountEvent.DobY -> {
                state = state.copy(dobY = state.dobY)
            }

            is AccountEvent.Submit -> {
                submitInfo()
            }
        }
    }

    private fun submitInfo() {
        val validationPan = validatePanInfo.executePAN(state.PAN)  // validation case for pan

        val validationDob =
            validatePanInfo.executeDob(state.dobD + "-" + state.dobM + "-" + state.dobY) // validation case for date of birth

        state = state.copy(errorDob = state.errorDob, errorPan = state.errorPan)

        if (!validationPan.isSuccessful || !validationDob.isSuccessful) { // if any of the fields are invalid
            // launch the coroutine
            viewModelScope.launch {
                channel.send(ValidationEvent.Failure)  // send the failure event
            }
        } else {
            viewModelScope.launch {
                channel.send(ValidationEvent.Success)  // in case of successful data , send the success event
            }
        }
    }

    sealed class ValidationEvent {
        object Success : ValidationEvent()
        object Failure : ValidationEvent()
    }
}
