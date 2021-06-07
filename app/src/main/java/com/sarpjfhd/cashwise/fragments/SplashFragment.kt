package com.sarpjfhd.cashwise.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.navigateSafe

class SplashFragment : Fragment() {
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //navigateToHomeAnyway()
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
        viewModel.userId = sharedPref!!.getInt("USER_ID", 0)
        val user = UserApplication.dbHelper.getUserData(viewModel.userId)
        if (user != null) {
            val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
            findNavController().navigateSafe(action)
        } else {
            val action = SplashFragmentDirections.actionSplashFragmentToLoginNewFragment()
            findNavController().navigateSafe(action)
        }
    }

    private fun navigateToHomeAnyway() {
        val action = SplashFragmentDirections.actionSplashFragmentToHomeFragment()
        findNavController().navigateSafe(action)
    }
}