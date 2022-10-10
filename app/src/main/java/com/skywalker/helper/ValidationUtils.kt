package com.skywalker.helper

import android.util.Patterns

object ValidationUtils {

    fun CharSequence?.isValidEmail() =
        !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

    internal fun isValidPassword(password: String): Boolean {
        if (password.length < 8) return false
        if (password.filter { it.isDigit() }.firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isUpperCase() }
                .firstOrNull() == null) return false
        if (password.filter { it.isLetter() }.filter { it.isLowerCase() }
                .firstOrNull() == null) return false
        if (password.filter { !it.isLetterOrDigit() }.firstOrNull() == null) return false

        return true
    }

    internal fun isValidConfirmPassword(password: String, confirmPassword: String): Boolean {
        if (password == confirmPassword)
            return true
        return false
    }

    internal fun isValidUserName(userName: String): Boolean {
        if (userName.length > 2)
            return true
        return false
    }
}