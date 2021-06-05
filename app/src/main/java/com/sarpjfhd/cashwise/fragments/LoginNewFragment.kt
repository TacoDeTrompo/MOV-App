package com.sarpjfhd.cashwise.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.models.LogInData
import com.sarpjfhd.cashwise.models.RestEngine
import com.sarpjfhd.cashwise.models.Service
import com.sarpjfhd.cashwise.models.User
import com.sarpjfhd.cashwise.navigateSafe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class LoginNewFragment : Fragment() {
    private lateinit var editCorreo: EditText
    private lateinit var editPassword: EditText
    private lateinit var buttonSignup: Button
    private lateinit var buttonLogIn: Button
    private val viewModel: MainViewModel by activityViewModels()
    val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editCorreo = view.findViewById(R.id.editCorreo)
        editPassword = view.findViewById(R.id.editTextPassword)
        buttonSignup = view.findViewById(R.id.buttonSignup)
        buttonLogIn = view.findViewById(R.id.button5)
        buttonSignup.setOnClickListener {
            val action = LoginNewFragmentDirections.actionLoginNewFragmentToSignupFragment()
            findNavController().navigateSafe(action)
        }
        buttonLogIn.setOnClickListener {
            val loginData = LogInData(editCorreo.text.toString(), editPassword.text.toString())
            login(object: ServiceCallback{
                override fun onSuccess(result: User) {
                    val base64 = result.encodedImage
                    val decodedImg = Base64.getDecoder().decode(base64)
                    result.imgArray = decodedImg
                    if (UserApplication.dbHelper.insertUserData(result)) {
                        val users = UserApplication.dbHelper.getListOfUserData()
                        //sharedPref!!.edit().putInt("USER_ID", users[0].idDB).apply()
                        viewModel.userId = users[0].idDB
                        val action = LoginNewFragmentDirections.actionLoginNewFragmentToHomeFragment()
                        findNavController().navigateSafe(action)
                    }
                }

            }, loginData)
        }
    }

    private fun login(apiServiceCallback: ServiceCallback, user: LogInData){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<User> = service.logIn(user)

        result.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(requireContext(), "Error iniciando sesion: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.body() != null) {
                    apiServiceCallback.onSuccess(response.body()!!)
                } else {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private interface ServiceCallback {
        fun onSuccess(result: User)
    }
}