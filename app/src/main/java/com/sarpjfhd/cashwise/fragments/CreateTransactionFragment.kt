package com.sarpjfhd.cashwise.fragments

import android.icu.util.CurrencyAmount
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.models.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.time.LocalDate

class CreateTransactionFragment : Fragment() {
    private lateinit var buttonReturn: Button
    private lateinit var buttonSave: Button
    private lateinit var editName: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var spinnerExType: Spinner
    private lateinit var editAmount: EditText
    private lateinit var editDescription: EditText
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
        return inflater.inflate(R.layout.fragment_create_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonReturn = view.findViewById(R.id.buttonTrCancel)
        buttonSave = view.findViewById(R.id.buttonTrSave)
        editName = view.findViewById(R.id.editTransactionName)
        spinnerType = view.findViewById(R.id.spinnerTransactionType)
        spinnerExType = view.findViewById(R.id.spinnerTransactionType2)
        editAmount = view.findViewById(R.id.editTransactionAmount)
        editDescription = view.findViewById(R.id.editTextTextMultiLine)
        buttonReturn.setOnClickListener {
            findNavController().navigateUp()
        }
        buttonSave.setOnClickListener {
            val name: String = editName.text.toString()
            val description: String = editDescription.text.toString()
            val amount: BigDecimal = editAmount.text.toString().toBigDecimal()
            val spinnerIndex = spinnerType.selectedItemPosition
            val spinnerExIndex = spinnerExType.selectedItemPosition
            val transactionType: TransactionTypes = TransactionTypes.fromInt(spinnerIndex)!!
            val expenseType: ExpenseType = ExpenseType.fromInt(spinnerExIndex)!!
            val profileId: Int = viewModel.profileId
            val tdata = TransactionData()
            tdata.name = name
            tdata.description = description
            tdata.profileId = profileId
            tdata.amount = amount
            tdata.expenseType = expenseType
            tdata.transactionType = transactionType
            when (transactionType) {
                TransactionTypes.EXPENSE -> {
                    val expense = TransactionFactory.createExpense(transactionType, name, amount, LocalDate.now(), expenseType, description, UserApplication.dbHelper.getProfile(profileId))
                    UserApplication.dbHelper.insertExpense(expense, profileId)
                }
                TransactionTypes.INGRESS -> {
                    val ingress = TransactionFactory.createIngress(transactionType, name, amount, LocalDate.now(), expenseType, description, UserApplication.dbHelper.getProfile(profileId))
                    UserApplication.dbHelper.insertIngress(ingress, profileId)
                }
            }
            uploadTransaction(tdata)
            findNavController().navigateUp()
        }
    }

    private fun uploadTransaction(transactionData: TransactionData){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseBody> = service.createTransaction(transactionData)

        result.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Error subiendo el perfil: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.body() == null) {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_LONG).show()
                } else {
                    //Toast.makeText(requireContext(), "El perfil ha sido subido", Toast.LENGTH_LONG).show()
                    findNavController().navigateUp()
                    setFragmentResult(
                        DraftUpdateFragment.REQUEST_KEY_SAVED, bundleOf(
                            DraftUpdateFragment.BUNDLE_KEY_SAVED to transactionData.idBD)
                    )
                }
            }
        })
    }
}