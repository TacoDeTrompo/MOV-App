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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.UserApplication.Companion.dbHelper
import com.sarpjfhd.cashwise.data.DataDbHelper
import com.sarpjfhd.cashwise.models.Profile
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver
import java.time.LocalDate

class CreateProfileFragment : Fragment() {
    private lateinit var editName: EditText
    private lateinit var editDescription: EditText
    private lateinit var buttonCancel: Button
    private lateinit var buttonCreatePr: Button
    private lateinit var viewColor: View
    private lateinit var colorPicker: ColorPickerPopup
    private lateinit var radio7: RadioButton
    private lateinit var radio15: RadioButton
    private lateinit var radio30: RadioButton
    private lateinit var radio60: RadioButton
    private lateinit var profile: Profile
    private val viewModel: MainViewModel by activityViewModels()
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
        viewColor = view.findViewById(R.id.viewColor)
        radio7 = view.findViewById(R.id.radio7dias)
        radio15 = view.findViewById(R.id.radio15dias)
        radio30 = view.findViewById(R.id.radio30dias)
        radio60 = view.findViewById(R.id.radio60dias)
        buttonCancel.setOnClickListener {
            findNavController().popBackStack()
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
                UserApplication.dbHelper.insertProfile(profile)
                findNavController().navigateUp()
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
}