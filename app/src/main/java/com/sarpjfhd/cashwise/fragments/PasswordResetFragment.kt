package com.sarpjfhd.cashwise.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.models.PasswordReset
import com.sarpjfhd.cashwise.models.RestEngine
import com.sarpjfhd.cashwise.models.Service
import com.sarpjfhd.cashwise.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordResetFragment : Fragment() {
    private lateinit var editOldPassword: EditText
    private lateinit var editNewPassword: EditText
    private lateinit var editRepeatPass: EditText
    private lateinit var btnReset: Button
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password_reset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editOldPassword = view.findViewById(R.id.editTextOldPassword)
        editNewPassword = view.findViewById(R.id.editTextNewPassword)
        editRepeatPass = view.findViewById(R.id.editTextRepeatNewPassword)
        btnReset = view.findViewById(R.id.button)

        btnReset.setOnClickListener {
            val newPass = editNewPassword.text.toString()
            val repeatPass = editRepeatPass.text.toString()
            val user = UserApplication.dbHelper.getUserData(viewModel.userId)
            val passRes = PasswordReset(user!!.token, newPass, user.cloudId)
            if (newPass == repeatPass) {
                editNewPassword.setBackgroundResource(0)
                editRepeatPass.setBackgroundResource(0)
                resetPass(object: ServiceCallback {
                    override fun onSuccess() {
                        findNavController().navigateUp()
                    }
                }, passRes)
            } else {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                editNewPassword.setBackgroundColor(Color.parseColor("#7F0E19"))
                editRepeatPass.setBackgroundColor(Color.parseColor("#7F0E19"))
            }
        }
    }

    private fun resetPass(apiServiceCallback: ServiceCallback, resetPass: PasswordReset) {
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseBody> = service.resetPassword(resetPass)

        result.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error durante el registro: ${t.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() != null) {
                    apiServiceCallback.onSuccess()
                } else {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private interface ServiceCallback {
        fun onSuccess()
    }
}