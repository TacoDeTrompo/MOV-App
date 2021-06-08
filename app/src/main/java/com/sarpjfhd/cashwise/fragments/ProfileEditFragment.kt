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
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.data.DataDbHelper
import com.sarpjfhd.cashwise.models.Profile
import com.sarpjfhd.cashwise.models.RestEngine
import com.sarpjfhd.cashwise.models.Service
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver
import java.time.LocalDate

class ProfileEditFragment : Fragment() {
    private lateinit var editName: EditText
    private lateinit var editDescription: EditText
    private lateinit var buttonCancel: Button
    private lateinit var buttonSaveDraft: Button
    private lateinit var viewColor: View
    private lateinit var colorPicker: ColorPickerPopup
    private lateinit var radio7: RadioButton
    private lateinit var radio15: RadioButton
    private lateinit var radio30: RadioButton
    private lateinit var radio60: RadioButton
    private lateinit var profile: Profile

    companion object {
        const val REQUEST_KEY_SAVED = "REQUEST_KEY_SAVED"
        const val BUNDLE_KEY_SAVED = "BUNDLE_KEY_SAVED"
    }

    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editName = view.findViewById(R.id.editTextTextPersonNameP)
        editDescription = view.findViewById(R.id.editDescripcionP)
        buttonCancel = view.findViewById(R.id.buttonCancelUP)
        buttonSaveDraft = view.findViewById(R.id.buttonUpdateProfile)
        viewColor = view.findViewById(R.id.viewColorP)
        radio7 = view.findViewById(R.id.radio7diasP)
        radio15 = view.findViewById(R.id.radio15diasP)
        radio30 = view.findViewById(R.id.radio30diasP)
        radio60 = view.findViewById(R.id.radio60diasP)
        buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        profile = Profile("", 0, LocalDate.now(), "", "")
        profile.idBD = viewModel.profileId

        getProfile(object: ServiceCallback{
            override fun onSuccess(result: Profile) {
                profile = result
                editName.setText(profile.profileName)
                editDescription.setText(profile.descrption)
                when (profile.dayRange){
                    7-> {
                        radio7.isChecked = true
                    }
                    15-> {
                        radio15.isChecked = true
                    }
                    30-> {
                        radio30.isChecked = true
                    }
                    60-> {
                        radio60.isChecked = true
                    }
                }
                viewColor.setBackgroundColor(profile.color.toInt())
            }

        }, viewModel.profileId, profile)


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
                profile.profileName = name
                profile.descrption = description
                profile.dayRange = dayRange
                profile.color = color.toString()
            }
            uploadProfile(object: ServiceCallbackPOST {
                override fun onSuccess(result: Boolean) {
                    findNavController().navigateUp()
                    //setFragmentResult(REQUEST_KEY_SAVED, bundleOf(BUNDLE_KEY_SAVED to profile.idBD))
                }

            } ,profile)
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

    private fun uploadProfile(serviceCallback: ServiceCallbackPOST,profile: Profile){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseBody> = service.updateProfile(profile)

        result.enqueue(object: Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Error subiendo el perfil: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() != null) {
                    Toast.makeText(requireContext(), "El borrador ha sido subido", Toast.LENGTH_LONG).show()
                    serviceCallback.onSuccess(true)
                } else {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun getProfile(apiServiceInterface: ServiceCallback, profileId: Int, profile: Profile){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Profile> = service.getProfile(profile)

        result.enqueue(object: Callback<Profile>{
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                val profile = response.body()
                apiServiceInterface.onSuccess(profile!!)
            }
        })
    }

    public interface ServiceCallback {
        fun onSuccess(result: Profile)
    }
    public interface ServiceCallbackPOST {
        fun onSuccess(result: Boolean)
    }
}