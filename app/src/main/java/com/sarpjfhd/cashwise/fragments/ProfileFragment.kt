package com.sarpjfhd.cashwise.fragments

import android.content.ClipDescription
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.UserApplication
import com.sarpjfhd.cashwise.models.*
import com.sarpjfhd.cashwise.navigateSafe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        getExpenses(object: ServiceCallbackExpenses{
            override fun onSuccess(result: MutableList<Expense>) {
                val rcListTransaction: RecyclerView = root.findViewById<RecyclerView>(R.id.recyclerView2)
                rcListTransaction.layoutManager = LinearLayoutManager(context2!!)
                transactionAdapter = TransactionRecyclerAdapter(context2!!, result)
                rcListTransaction.adapter = transactionAdapter
            }
        }, viewModel.profileId)

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
        var profile: Profile
        val list: MutableList<Ingress> = mutableListOf<Ingress>()
        val exList: MutableList<Expense> = mutableListOf<Expense>()
        getProfile(object: ServiceCallbackProfile{
            override fun onSuccess(result: Profile) {
                profile = result
                txtDate.text = profile?.startDate?.format(formatter)
                txtDescription.text = profile?.descrption
            }
        }, viewModel.profileId)
        getExpenses(object: ServiceCallbackExpenses{
            override fun onSuccess(result: MutableList<Expense>) {
                for (item in result) {
                    exList.add(item)
                }
            }
        }, viewModel.profileId)
        getIngresses(object: ServiceCallbackIngresses{
            override fun onSuccess(result: MutableList<Ingress>) {
                for (item in result) {
                    list.add(item)
                }
            }
        }, viewModel.profileId)

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


    private fun getProfile(apiServiceInterface: ServiceCallbackProfile, profileId: Int){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Profile> = service.getProfile(profileId)

        result.enqueue(object: Callback<Profile> {
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                var profile: Profile = response.body()!!
                apiServiceInterface.onSuccess(profile)
            }
        })
    }

    private fun getExpenses(apiServiceInterface: ServiceCallbackExpenses, profileId: Int){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Expense>> = service.getExpensesByProfile(profileId)

        result.enqueue(object: Callback<List<Expense>>{
            override fun onFailure(call: Call<List<Expense>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Expense>>, response: Response<List<Expense>>) {
                val arrayItems = response.body()
                val expenses: MutableList<Expense> = arrayItems!!.toMutableList()
                apiServiceInterface.onSuccess(expenses)
            }
        })
    }

    private fun getIngresses(apiServiceInterface: ServiceCallbackIngresses, profileId: Int){
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Ingress>> = service.getIngressesById(profileId)

        result.enqueue(object: Callback<List<Ingress>>{
            override fun onFailure(call: Call<List<Ingress>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Ingress>>, response: Response<List<Ingress>>) {
                val arrayItems = response.body()
                val ingresses: MutableList<Ingress> = arrayItems!!.toMutableList()
                apiServiceInterface.onSuccess(ingresses)
            }
        })
    }

    public interface ServiceCallbackProfile {
        fun onSuccess(result: Profile)
    }
    public interface ServiceCallbackExpenses {
        fun onSuccess(result: MutableList<Expense>)
    }
    public interface ServiceCallbackIngresses {
        fun onSuccess(result: MutableList<Ingress>)
    }


}