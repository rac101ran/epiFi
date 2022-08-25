package com.example.epifi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.*
import com.example.epifi.model.AccountEvent
import com.example.epifi.databinding.ActivityMainBinding
import com.example.epifi.viewmodel.ViewModelFi
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding

    private val viewModelFi by viewModels<ViewModelFi>()

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        initView()
        observers()
    }

    @DelicateCoroutinesApi
    private fun observers() {
        lifecycleScope.launchWhenStarted {
            viewModelFi.validatedFlow.collect { event ->    // listen/collect the send event from the view-model
                when (event) {
                    is ViewModelFi.ValidationEvent.Success -> {
                        // when it is a successful validation , show toast and end the activity
                        Toast.makeText(
                            this@MainActivity,
                            "Details Submitted Successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        // short delay for better user experience
                        GlobalScope.launch {
                            delay(400)
                            finish()
                        }

                    }
                    is ViewModelFi.ValidationEvent.Failure -> {
                        // when it is the failure validation , disable the NEXT button
                        binding.btNext.isEnabled = false
                        Log.e("message", "Invalid data")
                    }
                }
            }
        }
    }

    private fun initView() {

        binding.tvHeader2.text = resources.getString(R.string.title_PAN)
        binding.tvDesc.text = resources.getString(R.string.info_KYC)

        val state = viewModelFi.state
        binding.etPan.doAfterTextChanged {
            state.PAN = it.toString()
            binding.btNext.isEnabled = true
            viewModelFi.eventHandler(AccountEvent.PanChanged(it.toString()))  // handle the event of changes in pan number
        }

        binding.etDate.doAfterTextChanged {
            state.dobD = it.toString()
            binding.btNext.isEnabled = true
            viewModelFi.eventHandler(AccountEvent.DobD(it.toString()))   // handle the event of changes in dob
        }
        binding.etMonth.doAfterTextChanged {
            state.dobM = it.toString()
            binding.btNext.isEnabled = true
            viewModelFi.eventHandler(AccountEvent.DobM(it.toString()))
        }
        binding.etYear.doAfterTextChanged {
            state.dobY = it.toString()
            binding.btNext.isEnabled = true
            viewModelFi.eventHandler(AccountEvent.DobY(it.toString()))
        }
        binding.btNext.setOnClickListener { viewModelFi.eventHandler(AccountEvent.Submit) }  // submit the filled request of user
        binding.tvNoPan.setOnClickListener { finish() }  // if user doesn't have the pan number then end the activity
    }


}