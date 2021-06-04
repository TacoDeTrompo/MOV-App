package com.sarpjfhd.cashwise.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sarpjfhd.cashwise.MainViewModel
import com.sarpjfhd.cashwise.R
import com.sarpjfhd.cashwise.models.Advice
import com.sarpjfhd.cashwise.models.Profile
import com.sarpjfhd.cashwise.models.RestEngine
import com.sarpjfhd.cashwise.models.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdviceFragment : Fragment() {
    private lateinit var adviceAdapter: AdviceRecyclerAdapter
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_advice, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAdvices(object: ServiceCallback {
            override fun onSuccess(result: MutableList<Advice>) {
                val rcListAdvice: RecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView2)
                rcListAdvice.layoutManager = LinearLayoutManager(requireContext())
                adviceAdapter = AdviceRecyclerAdapter(requireContext(), arrayListOf())
                rcListAdvice.adapter = adviceAdapter
            }
        })
    }

    private fun getAdvices(apiServiceInterface: ServiceCallback) {
        val service: Service = RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Advice>> = service.getAdvices()

        result.enqueue(object: Callback<List<Advice>> {
            override fun onFailure(call: Call<List<Advice>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error obteniendo los mensajes", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Advice>>, response: Response<List<Advice>>) {
                val arrayItems = response.body()
                val advices: MutableList<Advice> = arrayItems!!.toMutableList()
                apiServiceInterface.onSuccess(advices)
            }
        })
    }

    private interface ServiceCallback {
        fun onSuccess(result: MutableList<Advice>)
    }
}