package com.sarpjfhd.cashwise.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.models.RestEngine
import com.sarpjfhd.cashwise.models.Service
import com.sarpjfhd.cashwise.models.User
import com.sarpjfhd.cashwise.navigateSafe
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import kotlin.jvm.Throws

class SignupFragment : Fragment() {

    private val activityResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK && it.data != null) {
                Thread {
                    val uri = it!!.data?.data ?: return@Thread
                    val input = requireContext().contentResolver.openInputStream(uri)
                    val inputData = getBytes(input!!)
                    imgArray = inputData!!
                    Glide.with(requireContext()).load(inputData).into(imgProfilePic)
                }.run()
            }
        }

    private lateinit var editUserName: EditText
    private lateinit var editFirstName: EditText
    private lateinit var editLastName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var editPasswordR: EditText
    private lateinit var imgProfilePic: ImageView
    private lateinit var btnRegister: Button
    private var imgArray: ByteArray = ByteArray(0)
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editFirstName = view.findViewById(R.id.editFirstName2)
        editLastName = view.findViewById(R.id.editLastName2)
        editUserName = view.findViewById(R.id.editUserName2)
        editEmail = view.findViewById(R.id.editEmail2)
        imgProfilePic = view.findViewById(R.id.imageViewProfile2)
        editPassword = view.findViewById(R.id.editTextPasswordSU)
        editPasswordR = view.findViewById(R.id.editTextPasswordRSU)
        btnRegister = view.findViewById(R.id.button2)

        imgProfilePic.setOnClickListener {
            Dexter.withContext(requireContext())
                .withPermissions(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        val report = p0 ?: return
                        if (report.areAllPermissionsGranted()) {
                            val intent = Intent(Intent.ACTION_PICK).apply {
                                type = "image/*"
                            }
                            activityResult.launch(intent)
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        print("aaa")
                    }
                }).check()
        }

        btnRegister.setOnClickListener {
            val user = User(
                editPassword.text.toString(),
                editFirstName.text.toString(),
                editLastName.text.toString(),
                editUserName.text.toString(),
                editEmail.text.toString(),
                imgArray,
                0,
                0
            )
            if (editPassword.text.toString() == editPasswordR.text.toString()){
                signUp(object : ServiceCallback {
                    override fun onSuccess(result: User) {
                        val base64 = result.encodedImage
                        val decodedImg = Base64.getDecoder().decode(base64)
                        result.imgArray = decodedImg
                        if (UserApplication.dbHelper.insertUserData(result)) {
                            val users = UserApplication.dbHelper.getListOfUserData()
                            viewModel.userId = users[0].idDB
                            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
                            sharedPref!!.edit().putInt("USER_ID", users[0].idDB).apply()
                            val action = SignupFragmentDirections.actionSignupFragmentToHomeFragment()
                            findNavController().navigateSafe(action)
                        }
                    }
                }, user)
            }
        }
    }

    private fun signUp(apiServiceCallback: ServiceCallback, user: User) {
        val imgArray = user.imgArray
        val base64: String = Base64.getEncoder().encodeToString(imgArray)
        user.encodedImage = base64
        user.imgArray = ByteArray(0)

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<User> = service.signUp(user)

        result.enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(
                    requireContext(),
                    "Error durante el registro: ${t.localizedMessage}",
                    Toast.LENGTH_LONG
                ).show()
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

    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also({ len = it }) != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }
}