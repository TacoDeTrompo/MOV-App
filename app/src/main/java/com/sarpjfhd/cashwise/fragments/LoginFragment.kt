package com.sarpjfhd.cashwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R

class LoginFragment : Fragment() {
    private lateinit var editCorreo: EditText
    private lateinit var buttonSignup: Button
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editCorreo = view.findViewById(R.id.editCorreo)
        buttonSignup = view.findViewById(R.id.buttonSignup)
        buttonSignup.setOnClickListener {
            //val action = HomeFragmentDirections.actionHomeFragmentToSignupFragment()
            //findNavController().navigateSafe(action)
        }
        viewModel.modelo.observe(viewLifecycleOwner) {
            editCorreo.setText(it)
        }
    }
}