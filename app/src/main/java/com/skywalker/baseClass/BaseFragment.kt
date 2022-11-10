package com.skywalker.baseClass

import android.content.Intent
import androidx.fragment.app.Fragment
import com.skywalker.ui.main.MainActivity

open class BaseFragment(layout : Int) : Fragment(layout) {

    fun redirectToLogin(){
        val intent = Intent(requireActivity(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}