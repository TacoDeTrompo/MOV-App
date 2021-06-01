package com.sarpjfhd.cashwise.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sarpjfhd.cashwise.R

class DeleteProfileFragment : Fragment() {
    private lateinit var buttonDelPrCancel: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_delete_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonDelPrCancel = view.findViewById(R.id.buttonDelPrRegresar)
        buttonDelPrCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}