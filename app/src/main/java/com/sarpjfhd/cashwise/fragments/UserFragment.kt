package com.sarpjfhd.cashwise.fragments

import android.app.Activity
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
import com.sarpjfhd.cashwise.models.*
import com.sarpjfhd.cashwise.navigateSafe
import okhttp3.ResponseBody
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.*
import kotlin.jvm.Throws

class UserFragment : Fragment() {

    private val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            Thread {
                val uri = it!!.data?.data ?: return@Thread
                val input = requireContext().contentResolver.openInputStream(uri)
                val inputData = getBytes(input!!)
                user.imgArray = inputData!!
                //pls()
                Glide.with(requireContext()).load(inputData).into(imgProfilePic)
            }.run()
        }
    }

    private lateinit var editUsername: EditText
    private lateinit var editFirstName: EditText
    private lateinit var editLastName: EditText
    private lateinit var editEmail: EditText
    private lateinit var imgProfilePic: ImageView
    private lateinit var btnResetPass: Button
    private lateinit var btnSave: Button
    private lateinit var btnLogOut: Button
    private lateinit var user: User
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editFirstName = view.findViewById(R.id.editFirstName)
        editLastName = view.findViewById(R.id.editLastName)
        editUsername = view.findViewById(R.id.editUserName)
        editEmail = view.findViewById(R.id.editEmail)
        imgProfilePic = view.findViewById(R.id.imageViewProfile)
        btnResetPass = view.findViewById(R.id.buttonResetPass)
        btnSave = view.findViewById(R.id.buttonSave)
        btnLogOut = view.findViewById(R.id.buttonLogOut)
        try {
            user = UserApplication.dbHelper.getUserData(viewModel.userId)!!
            editUsername.setText(user.username)
            editFirstName.setText(user.fullName)
            editLastName.setText(user.lastName)
            editEmail.setText(user.email)
            activity?.runOnUiThread {
                Glide.with(requireContext()).load(user.imgArray).into(imgProfilePic)
            }
        } catch (e: Exception) {
            user = User("sasas", "", "", "", "", ByteArray(0), 1, 1)
        }

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
        btnSave.setOnClickListener {
            user.username = editUsername.text.toString()
            user.fullName = editFirstName.text.toString()
            user.lastName = editLastName.text.toString()
            user.email = editEmail.text.toString()
            val imgArray = user.imgArray
            val base64: String = Base64.getEncoder().encodeToString(imgArray)
            user.encodedImage = base64
            uploadUserData(object: ServiceCallback{
                override fun onSuccess(user: User) {
                    val base64 = user.encodedImage.toString()
                    val decodedImg = Base64.getDecoder().decode(base64)
                    user.imgArray = decodedImg
                    UserApplication.dbHelper.updateUser(user)
                }

            },user)
        }
        btnLogOut.setOnClickListener {
            UserApplication.dbHelper.deleteUser(user.idDB)
            findNavController().navigateUp()
        }
        btnResetPass.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToPasswordResetFragment()
            findNavController().navigateSafe(action)
        }
    }

    private fun uploadUserData(apiServiceCallback: ServiceCallback, user: User){

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseBody> = service.updateUser(user)

        result.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Error actualizando datos de usuario: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body()!! != null) {
                    Toast.makeText(requireContext(), "El usuario ha sido actualizado", Toast.LENGTH_LONG).show()
                    apiServiceCallback.onSuccess(user)
                } else {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    fun pls() {
        val imgArray = user.imgArray
        val base64: String = Base64.getEncoder().encodeToString(imgArray)

        val advice = Advice(ByteArray(0), "Decida cuáles son sus prioridades", "Conozca cómo dar prioridad a sus metas de ahorro para tener una idea clara de dónde empezar a ahorrar.\n" +
                "ahorita te mando las fotos respectivas.")

        advice.encodedImage = base64

        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseBody> = service.uploadAdvice(advice)

        result.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Error actualizando datos de usuario: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                response.body()
                /*if (response.body()!!) {
                    Toast.makeText(requireContext(), "El usuario ha sido actualizado", Toast.LENGTH_LONG).show()
                    //apiServiceCallback.onSuccess(user)
                } else {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_LONG).show()
                }*/
                Toast.makeText(requireContext(), "El usuario ha sido actualizado", Toast.LENGTH_LONG).show()
            }

        })
    }

    private interface ServiceCallback {
        fun onSuccess(user: User)
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