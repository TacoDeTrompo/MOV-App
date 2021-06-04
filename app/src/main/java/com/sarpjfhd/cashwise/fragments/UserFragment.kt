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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.models.User
import okio.IOException
import java.io.ByteArrayOutputStream
import java.io.InputStream
import kotlin.jvm.Throws

class UserFragment : Fragment() {

    private val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            Thread {
                val uri = it!!.data?.data ?: return@Thread
                val input = requireContext().contentResolver.openInputStream(uri)
                val inputData = getBytes(input!!)
                user.imgArray = inputData!!
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
        user = User("sasas", "", "", "", "", ByteArray(0), 1)
        /*val user = UserApplication.dbHelper.getUserData(viewModel.userId)
        if (user != null) {
            editUsername.setText(user.username)
            editFirstName.setText(user.fullName)
            editLastName.setText(user.lastName)
            editEmail.setText(user.email)
            activity?.runOnUiThread {
                Glide.with(requireContext()).load(user.imgArray).into(imgProfilePic)
            }
        }*/
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