package com.example.epifi.usecases

import android.util.Log
import java.util.*

class ValidatePanInfo {
    fun executePAN(pan: String): ValidateResult {
        if (pan.isBlank()) {
            return ValidateResult(false, "Pan number is empty")
        }
        // a valid pan is  XXXXXNNNNX , where X is letter & N is digit
        return if (pan.length == 10 && pan.substring(0, 5)
                .all { it.isUpperCase() } && pan.substring(5, 9)
                .all { it.isDigit() } && pan[9].isUpperCase()
        ) {
            ValidateResult(true, "")
        } else {
            ValidateResult(false, "Invalid Pan Number")
        }
    }

    fun executeDob(dob: String): ValidateResult {
        if (dob.isEmpty()) {
            return ValidateResult(false, "Date of birth is empty")
        }
        val lDob = dob.split('-')

        val calendarY = Calendar.getInstance().get(Calendar.YEAR)
        val calendarM = Calendar.getInstance().get(Calendar.MONTH)

        return if (lDob.size == 3 && lDob.all { it.isNotEmpty() } && above18(
                12 - lDob[1].toInt() + calendarM,
                calendarY
            ) >= 18) {

            if (lDob[1].toInt() == 2 && ((lDob[0].toInt() in 1..29 && lDob[2].toInt() % 4 == 0) || (lDob[0].toInt() in 1..28))) {

                ValidateResult(true, "")  // feb & leap year

            } else if (lDob[1].toInt() in 1..7 && ((lDob[1].toInt() % 2 == 0 && lDob[0].toInt() in 1..30) || (lDob[1].toInt() % 2 != 0 && lDob[0].toInt() in 1..31))) {

                ValidateResult(
                    true,
                    ""
                )  // rest odd months have 31 days & even have 30 days , till july

            } else if (lDob[1].toInt() in 8..12 && ((lDob[1].toInt() % 2 == 0 && lDob[0].toInt() in 1..31) || (lDob[1].toInt() % 2 != 0 && lDob[0].toInt() in 1..30))) {

                ValidateResult(true, "")  // opposite parity from august to december

            } else {
                ValidateResult(false, "Invalid date of birth")
            }
        } else {
            ValidateResult(false, "Invalid date of birth format")
        }
    }

    fun above18(rest: Int, yrs: Int = 0): Int {
        var tot = 0
        if (rest >= 12) tot += 1
        return tot + yrs
    }

}