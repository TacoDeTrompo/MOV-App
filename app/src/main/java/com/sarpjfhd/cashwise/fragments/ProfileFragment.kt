package com.sarpjfhd.cashwise.fragments

import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.models.Expense
import com.sarpjfhd.cashwise.models.Ingress
import com.sarpjfhd.cashwise.models.User
import com.sarpjfhd.cashwise.navigateSafe
import java.math.BigDecimal
import java.time.format.DateTimeFormatter

class ProfileFragment : Fragment() {
    private lateinit var buttonAddTransaction: FloatingActionButton
    private lateinit var buttonReturn: Button
    private lateinit var txtTotalAmount: TextView
    private lateinit var txtDate: TextView
    private lateinit var txtDescription: TextView
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
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        val rcListTransaction: RecyclerView = root.findViewById<RecyclerView>(R.id.recyclerView2)
        rcListTransaction.layoutManager = LinearLayoutManager(this.context2!!)
        this.transactionAdapter = TransactionRecyclerAdapter(this.context2!!, UserApplication.dbHelper.getListOfExpense(viewModel.profileId))
        rcListTransaction.adapter = this.transactionAdapter

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonAddTransaction = view.findViewById(R.id.buttonAddTransaction)
        buttonReturn = view.findViewById(R.id.buttonPrRegresar2)
        txtTotalAmount = view.findViewById(R.id.textTotalExpenses)
        txtDate = view.findViewById(R.id.textFecha)
        txtDescription = view.findViewById(R.id.textDescripcion)
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val profile = UserApplication.dbHelper.getProfile(viewModel.profileId)
        txtDate.text = profile?.startDate?.format(formatter)
        txtDescription.text = profile?.descrption
        val list = UserApplication.dbHelper.getListOfIngress(viewModel.profileId)
        val exList = UserApplication.dbHelper.getListOfExpense(viewModel.profileId)
        var amount: BigDecimal = BigDecimal(0)
        var expenses: BigDecimal = BigDecimal(0)
        for (ingress in list) {
            amount += ingress.amount
        }
        for (expense in exList) {
            expenses += expense.amount
        }
        var total = amount - expenses
        txtTotalAmount.text = "Total: $${total.toString()}"
        buttonAddTransaction.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToCreateTransactionFragment()
            findNavController().navigateSafe(action)
        }
        buttonReturn.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}