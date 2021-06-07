package com.sarpjfhd.cashwise.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.models.*
import com.sarpjfhd.cashwise.navigateSafe
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.BigInteger

class TransactionListFragment(private var expenses: MutableList<Expense>, private var ingress: MutableList<Ingress>, private var isExpense: Boolean) : Fragment(), MoveToEditTransactionFragment {
    private lateinit var total: TextView
    private var transactionAdapter:TransactionRecyclerAdapter? = null
    private val viewModel: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        total = view.findViewById(R.id.textTotalTrans)

        when (isExpense) {
            true -> {
                val rcListTransaction: RecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView2)
                rcListTransaction.layoutManager = LinearLayoutManager(requireContext())
                transactionAdapter = TransactionRecyclerAdapter(requireContext(), expenses, arrayListOf(), true, this)
                rcListTransaction.adapter = transactionAdapter
            }
            false -> {
                val rcListTransaction: RecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView2)
                rcListTransaction.layoutManager = LinearLayoutManager(requireContext())
                transactionAdapter = TransactionRecyclerAdapter(requireContext(), arrayListOf(), ingress, false, this)
                rcListTransaction.adapter = transactionAdapter
            }
        }

        when (isExpense) {
            true -> {
                var sum = BigDecimal(BigInteger.ZERO)
                for (item in expenses) {
                    sum += item.amount
                }
                total.text = "$${sum.toString()}"
            }
            false -> {
                var sum = BigDecimal(BigInteger.ZERO)
                for (item in ingress) {
                    sum += item.amount
                }
                total.text = "$${sum.toString()}"
            }
        }
    }

    fun deleteTransaction(apiServiceInterface: ServiceCallback, transactionId: Int) {
        val tdata = TransactionData()
        tdata.idBD = transactionId
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<ResponseBody> = service.deleteTransaction(tdata)

        result.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                apiServiceInterface.onSuccess(true)
            }
        })
    }

    interface ServiceCallback {
        fun onSuccess(result: Boolean)
    }

    override fun onUpdateClick(isExpense: Boolean, transactionId: Int) {
        viewModel.currentTransactionState = isExpense
        viewModel.transactionId = transactionId
        val action = ProfileFragmentDirections.actionProfileFragmentToEditTransactionFragment()
        findNavController().navigateSafe(action)
    }

    override fun onDeleteClick(transactionId: Int, transactions: MutableList<Transaction>, index: Int) {
        val singleChoiceDialog = AlertDialog.Builder(requireContext())
            .setTitle("¿Esta seguro que quiere eliminar esta transacción?")
            .setPositiveButton("Si"){ _,_ ->
                deleteTransaction(object: ServiceCallback {
                    override fun onSuccess(result: Boolean) {
                        transactionAdapter?.deleteItem(index)
                    }
                }, transactionId)
            }
            .setNegativeButton("Cancelar"){ _,_ ->

                //Toast.makeText(context,"Cancel",Toast.LENGTH_SHORT).show()
            }.create()

        singleChoiceDialog.show()
    }

    fun getTotalIngresses(apiServiceInterface: ServiceCallback, profile: Profile) {
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<AmountData> = service.getTotalIngresses(profile)

        result.enqueue(object: Callback<AmountData> {
            override fun onFailure(call: Call<AmountData>, t: Throwable) {
                Toast.makeText(requireContext(), "Error subiendo el perfil: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<AmountData>, response: Response<AmountData>) {
                apiServiceInterface.onSuccess(true)
            }
        })
    }

    fun getTotalExpenses(apiServiceInterface: ServiceCallback, profile: Profile) {
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<AmountData> = service.getTotalExpenses(profile)

        result.enqueue(object: Callback<AmountData> {
            override fun onFailure(call: Call<AmountData>, t: Throwable) {
                Toast.makeText(requireContext(), "Error subiendo el perfil: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<AmountData>, response: Response<AmountData>) {
                apiServiceInterface.onSuccess(true)
            }
        })
    }

}