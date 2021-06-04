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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.models.*
import com.sarpjfhd.cashwise.navigateSafe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.time.LocalDate

class EditTransactionFragment : Fragment() {
    private lateinit var buttonReturn: Button
    private lateinit var buttonSave: Button
    private lateinit var editName: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var spinnerExType: Spinner
    private lateinit var editAmount: EditText
    private lateinit var editDescription: EditText
    private val viewModel: MainViewModel by activityViewModels()
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
        when (viewModel.currentTransactionState){
            true -> {
                getExpense(object: ServiceCallback {
                    override fun onSuccess(result: Expense) {
                        editName.setText(result.name)
                        spinnerType.setSelection(result.transactionType.value)
                        spinnerExType.setSelection(result.type.value)
                        editDescription.setText(result.description)
                        editAmount.setText(result.amount.toString())
                    }

                    override fun onSuccess(result: Ingress) {
                        TODO("Not yet implemented")
                    }

                }, viewModel.transactionId)
            }
            false -> {
                getIngress(object: ServiceCallback {
                    override fun onSuccess(result: Expense) {
                        TODO("Not yet implemented")
                    }

                    override fun onSuccess(result: Ingress) {
                        editName.setText(result.name)
                        spinnerType.setSelection(result.transactionType.value)
                        spinnerExType.setSelection(0)
                        editDescription.setText(result.description)
                        editAmount.setText(result.amount.toString())
                    }

                }, viewModel.transactionId)
            }
        }
        buttonSave.setOnClickListener {
            /*val name: String = editName.text.toString()
            val description: String = editDescription.text.toString()
            val amount: BigDecimal = editAmount.text.toString().toBigDecimal()
            val spinnerIndex = spinnerType.selectedItemPosition
            val spinnerExIndex = spinnerExType.selectedItemPosition
            val transactionType: TransactionTypes = TransactionTypes.fromInt(spinnerIndex)!!
            val expenseType: ExpenseType = ExpenseType.fromInt(spinnerExIndex)!!
            val profileId: Int = viewModel.profileId
            when (transactionType) {
                TransactionTypes.EXPENSE -> {
                    val expense = TransactionFactory.createExpense(transactionType, name, amount, LocalDate.now(), expenseType, description, UserApplication.dbHelper.getProfile(profileId))
                    UserApplication.dbHelper.insertExpense(expense, profileId)
                }
                TransactionTypes.INGRESS -> {
                    val ingress = TransactionFactory.createIngress(transactionType, name, amount, LocalDate.now(), expenseType, description, UserApplication.dbHelper.getProfile(profileId))
                    UserApplication.dbHelper.insertIngress(ingress, profileId)
                }
            }*/
            val name: String = editName.text.toString()
            val description: String = editDescription.text.toString()
            val amount: BigDecimal = editAmount.text.toString().toBigDecimal()
            val spinnerIndex = spinnerType.selectedItemPosition
            val spinnerExIndex = spinnerExType.selectedItemPosition
            val transactionType: TransactionTypes = TransactionTypes.fromInt(spinnerIndex)!!
            val expenseType: ExpenseType = ExpenseType.fromInt(spinnerExIndex)!!
            val profileId: Int = viewModel.profileId
            when (transactionType) {
                TransactionTypes.EXPENSE -> {
                    val expense = TransactionFactory.createExpense(transactionType, name, amount, LocalDate.now(), expenseType, description, UserApplication.dbHelper.getProfile(profileId))
                    updateExpense(object: ServiceCallbackPOST {
                        override fun onSuccess(result: Boolean) {
                            TODO("Not yet implemented")
                        }
                    }, expense)
                }
                TransactionTypes.INGRESS -> {
                    val ingress = TransactionFactory.createIngress(transactionType, name, amount, LocalDate.now(), expenseType, description, UserApplication.dbHelper.getProfile(profileId))
                    updateIngress(object: ServiceCallbackPOST{
                        override fun onSuccess(result: Boolean) {
                            TODO("Not yet implemented")
                        }
                    }, ingress)
                }
            }
            findNavController().navigateUp()
        }
    }

    private fun updateExpense(serviceCallback: ServiceCallbackPOST,expense: Expense){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Boolean> = service.updateExpense(expense)

        result.enqueue(object: Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(requireContext(), "Error subiendo el perfil: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.body()!!) {
                    Toast.makeText(requireContext(), "El borrador ha sido subido", Toast.LENGTH_LONG).show()
                    serviceCallback.onSuccess(response.body()!!)
                } else {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun updateIngress(serviceCallback: ServiceCallbackPOST,ingress: Ingress){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Boolean> = service.updateIngress(ingress)

        result.enqueue(object: Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Toast.makeText(requireContext(), "Error subiendo el perfil: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.body()!!) {
                    Toast.makeText(requireContext(), "El borrador ha sido subido", Toast.LENGTH_LONG).show()
                    serviceCallback.onSuccess(response.body()!!)
                } else {
                    Toast.makeText(requireContext(), "Error del servidor", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun getExpense(apiServiceInterface: ServiceCallback, expenseId: Int){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Expense> = service.getExpenses(expenseId)

        result.enqueue(object: Callback<Expense> {
            override fun onFailure(call: Call<Expense>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Expense>, response: Response<Expense>) {
                val expense = response.body()
                apiServiceInterface.onSuccess(expense!!)
            }
        })
    }

    private fun getIngress(apiServiceInterface: ServiceCallback, ingressId: Int){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Ingress> = service.getIngress(ingressId)

        result.enqueue(object: Callback<Ingress> {
            override fun onFailure(call: Call<Ingress>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Ingress>, response: Response<Ingress>) {
                val ingress = response.body()
                apiServiceInterface.onSuccess(ingress!!)
            }
        })
    }

    public interface ServiceCallback {
        fun onSuccess(result: Expense)
        fun onSuccess(result: Ingress)
    }
    public interface ServiceCallbackPOST {
        fun onSuccess(result: Boolean)
    }
}