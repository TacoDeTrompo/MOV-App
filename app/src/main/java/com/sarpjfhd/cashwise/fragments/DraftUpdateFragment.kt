package com.sarpjfhd.cashwise.fragments

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
import com.sarpjfhd.cashwise.models.Profile
import com.sarpjfhd.cashwise.models.RestEngine
import com.sarpjfhd.cashwise.models.Service
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver

class DraftUpdateFragment : Fragment() {
    private lateinit var editName: EditText
    private lateinit var editDescription: EditText
    private lateinit var buttonCancel: Button
    private lateinit var buttonCreatePr: Button
    private lateinit var buttonSaveDraft: Button
    private lateinit var buttonDeleteDraft: Button
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
        return inflater.inflate(R.layout.fragment_draft_update, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editName = view.findViewById(R.id.editTextTextPersonNameD)
        editDescription = view.findViewById(R.id.editDescripcionD)
        buttonCancel = view.findViewById(R.id.buttonCancelD)
        buttonCreatePr = view.findViewById(R.id.buttonCreatePrD)
        buttonSaveDraft = view.findViewById(R.id.buttonSaveDraft2)
        buttonDeleteDraft = view.findViewById(R.id.buttonDeleteDraft)
        viewColor = view.findViewById(R.id.viewColorD)
        radio7 = view.findViewById(R.id.radio7diasD)
        radio15 = view.findViewById(R.id.radio15diasD)
        radio30 = view.findViewById(R.id.radio30diasD)
        radio60 = view.findViewById(R.id.radio60diasD)
        buttonCancel.setOnClickListener {
            findNavController().popBackStack()
        }
        profile = UserApplication.dbHelper.getProfile(viewModel.profileId)!!
        editName.setText(profile?.profileName)
        editDescription.setText(profile?.descrption)
        when (profile?.dayRange){
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
        buttonCreatePr.setOnClickListener {
            uploadProfile(object: ServiceCallback {
                override fun onSuccess(result: ResponseBody?) {
                    if (result != null) {
                        //Toast.makeText(requireContext(), "El borrador ha sido subido", Toast.LENGTH_LONG).show()
                        UserApplication.dbHelper.deleteProfile(viewModel.profileId)
                        findNavController().navigateUp()
                    } else {
                        Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_LONG).show()
                    }
                }

            }, profile)
        }
        viewColor.setBackgroundColor(profile?.color?.toInt()!!)
        buttonSaveDraft.setOnClickListener {
            val name: String = editName.text.toString()
            val description: String = editDescription.text.toString()
            var dayRange: Int = 0
            when {
                radio7.isChecked -> {
                    dayRange = 7
                }
                radio15.isChecked -> {
                    dayRange = 15
                }
                radio30.isChecked -> {
                    dayRange = 30
                }
                radio60.isChecked -> {
                    dayRange = 60
                }
            }
            val color: Int = (viewColor.background as ColorDrawable).color
            if (dayRange != 0) {
                profile.makeNew(name, dayRange, color.toString(), description, viewModel.profileId)
                UserApplication.dbHelper.updateProfile(profile)
                findNavController().navigateUp()
                //setFragmentResult(REQUEST_KEY_SAVED, bundleOf(BUNDLE_KEY_SAVED to profile.idBD))
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

        buttonDeleteDraft.setOnClickListener {
            UserApplication.dbHelper.deleteProfile(profile.idBD)
            findNavController().navigateUp()
            //setFragmentResult(REQUEST_KEY_SAVED, bundleOf(BUNDLE_KEY_SAVED to profile.idBD))
        }
    }

    override fun onDestroy() {
        try {
            colorPicker.dismiss()
        } catch (e: java.lang.Exception){

        }
        super.onDestroy()
    }

    private fun uploadProfile(apiServiceCallback: ServiceCallback, profile: Profile){
        profile.userID = UserApplication.dbHelper.getUserData(viewModel.userId)!!.cloudId
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseBody> = service.createProfile(profile)

        result.enqueue(object: Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Error subiendo el perfil: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                apiServiceCallback.onSuccess(response.body())
            }

        })
    }

    private interface ServiceCallback {
        fun onSuccess(result: ResponseBody?)
    }
}