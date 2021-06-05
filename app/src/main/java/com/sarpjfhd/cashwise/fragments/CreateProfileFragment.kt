package com.sarpjfhd.cashwise.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.UserApplication.Companion.dbHelper
import com.sarpjfhd.cashwise.data.DataDbHelper
import com.sarpjfhd.cashwise.models.Profile
import com.sarpjfhd.cashwise.models.RestEngine
import com.sarpjfhd.cashwise.models.Service
import com.sarpjfhd.cashwise.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver
import java.time.LocalDate

class CreateProfileFragment : Fragment() {
    private lateinit var editName: EditText
    private lateinit var editDescription: EditText
    private lateinit var buttonCancel: Button
    private lateinit var buttonCreatePr: Button
    private lateinit var buttonSaveDraft: Button
    private lateinit var viewColor: View
    private lateinit var colorPicker: ColorPickerPopup
    private lateinit var radio7: RadioButton
    private lateinit var radio15: RadioButton
    private lateinit var radio30: RadioButton
    private lateinit var radio60: RadioButton
    private lateinit var profile: Profile
    private val viewModel: MainViewModel by activityViewModels()

    companion object {
        const val REQUEST_KEY_SAVED = "REQUEST_KEY_SAVED"
        const val BUNDLE_KEY_SAVED = "BUNDLE_KEY_SAVED"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editName = view.findViewById(R.id.editTextTextPersonName)
        editDescription = view.findViewById(R.id.editDescripcion)
        buttonCancel = view.findViewById(R.id.buttonCancel)
        buttonCreatePr = view.findViewById(R.id.buttonCreatePr)
        buttonSaveDraft = view.findViewById(R.id.buttonSaveDraftP)
        viewColor = view.findViewById(R.id.viewColor)
        radio7 = view.findViewById(R.id.radio7dias)
        radio15 = view.findViewById(R.id.radio15dias)
        radio30 = view.findViewById(R.id.radio30dias)
        radio60 = view.findViewById(R.id.radio60dias)
        buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        buttonSaveDraft.setOnClickListener {
            val name: String = editName.text.toString()
            val description: String = editDescription.text.toString()
            var dayRange: Int = 0
            if (radio7.isChecked) {
                dayRange = 7
            } else if (radio15.isChecked) {
                dayRange = 15
            } else if (radio30.isChecked) {
                dayRange = 30
            } else if (radio60.isChecked) {
                dayRange = 60
            }
            val color: Int = (viewColor.background as ColorDrawable).color
            if (dayRange != 0) {
                profile = Profile(name, dayRange, LocalDate.now(), color.toString(), description)
                UserApplication.dbHelper.insertProfile(profile)
                findNavController().navigateUp()
            }
        }
        buttonCreatePr.setOnClickListener {
            val name: String = editName.text.toString()
            val description: String = editDescription.text.toString()
            var dayRange: Int = 0
            if (radio7.isChecked) {
                dayRange = 7
            } else if (radio15.isChecked) {
                dayRange = 15
            } else if (radio30.isChecked) {
                dayRange = 30
            } else if (radio60.isChecked) {
                dayRange = 60
            }
            val color: Int = (viewColor.background as ColorDrawable).color
            if (dayRange != 0) {
                profile = Profile(name, dayRange, LocalDate.now(), color.toString(), description)
                uploadProfile(profile)
            }
        }
        viewColor.setOnClickListener {
            var observer = object: ColorPickerObserver() {
                override fun onColorPicked(color: Int) {
                    viewColor.setBackgroundColor(color)
                }

                fun onColor(color: Int, fromUser: Boolean) {

                }
            }
            var draw: ColorDrawable = viewColor.background as ColorDrawable
            colorPicker = ColorPickerPopup.Builder(view.context)
                .initialColor(draw.color)
                .enableBrightness(true)
                .enableAlpha(false)
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
            colorPicker.show(viewColor, observer)
        }

        radio7.setOnClickListener {
            radio15.isChecked = false
            radio30.isChecked = false
            radio60.isChecked = false
        }
        radio15.setOnClickListener {
            radio7.isChecked = false
            radio30.isChecked = false
            radio60.isChecked = false
        }
        radio30.setOnClickListener {
            radio15.isChecked = false
            radio7.isChecked = false
            radio60.isChecked = false
        }
        radio60.setOnClickListener {
            radio15.isChecked = false
            radio30.isChecked = false
            radio7.isChecked = false
        }
    }

    override fun onDestroy() {
        try {
            colorPicker.dismiss()
        } catch (e: java.lang.Exception){

        }
        super.onDestroy()
    }

    private fun uploadProfile(profile: Profile){
        profile.userID = UserApplication.dbHelper.getUserData(viewModel.userId)!!.cloudId
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseBody> = service.createProfile(profile)

        result.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Error subiendo el perfil: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() == null) {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "El perfil ha sido subido", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                    setFragmentResult(
                        DraftUpdateFragment.REQUEST_KEY_SAVED, bundleOf(
                            DraftUpdateFragment.BUNDLE_KEY_SAVED to profile.idBD)
                    )
                }
            }

        })
    }
}