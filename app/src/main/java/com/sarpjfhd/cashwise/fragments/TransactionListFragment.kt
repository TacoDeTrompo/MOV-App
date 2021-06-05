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
    private var context2: Context? = null
    private val viewModel: MainViewModel by activityViewModels()
    override fun onAttach(context: Context) {
        super.onAttach(context)

        this.context2 =  context

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root =  inflater.inflate(R.layout.fragment_transaction_list, container, false)

        when (isExpense) {
            true -> {
                val rcListTransaction: RecyclerView = root.findViewById<RecyclerView>(R.id.recyclerView2)
                rcListTransaction.layoutManager = LinearLayoutManager(context2!!)
                transactionAdapter = TransactionRecyclerAdapter(context2!!, expenses, arrayListOf(), true, this)
                rcListTransaction.adapter = transactionAdapter
            }
            false -> {
                val rcListTransaction: RecyclerView = root.findViewById<RecyclerView>(R.id.recyclerView2)
                rcListTransaction.layoutManager = LinearLayoutManager(context2!!)
                transactionAdapter = TransactionRecyclerAdapter(context2!!, arrayListOf(), ingress, false, this)
                rcListTransaction.adapter = transactionAdapter
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        total = view.findViewById(R.id.textTotalTrans)

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

    override fun onUpdateClick() {
        val action = ProfileFragmentDirections.actionProfileFragmentToEditTransactionFragment()
        findNavController().navigateSafe(action)
    }

    override fun onDeleteClick(transactionId: Int, transactions: MutableList<Transaction>, index: Int) {
        val options =  arrayOf("First Item", "Second Item", "Third Item")
        val singleChoiceDialog = AlertDialog.Builder(requireContext())
            .setTitle("¿Esta seguro que quiere eliminar esta transacción?")
            .setSingleChoiceItems(options, 0){dialogInterface, i ->

                Toast.makeText(context, "You clicked on ${options[i]}", Toast.LENGTH_SHORT).show()

            }
            .setPositiveButton("Aceptar"){ _,_ ->
                deleteTransaction(object: ServiceCallback {
                    override fun onSuccess(result: Boolean) {
                        transactionAdapter?.deleteItem(index)
                    }
                }, transactionId)
            }
            .setNegativeButton("Cancelar"){ _,_ ->

                Toast.makeText(context,"Cancel",Toast.LENGTH_SHORT).show()
            }.create()

        singleChoiceDialog.show()
    }
}